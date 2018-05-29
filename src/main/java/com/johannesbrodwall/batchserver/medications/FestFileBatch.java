package com.johannesbrodwall.batchserver.medications;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

import org.eaxy.Document;
import org.eaxy.Element;
import org.eaxy.ElementSet;
import org.eaxy.Xml;

import com.johannesbrodwall.batchserver.BatchServerContext;
import com.johannesbrodwall.batchserver.batchfile.BatchFile;
import com.johannesbrodwall.batchserver.batchfile.BatchFileRepository;
import com.johannesbrodwall.batchserver.batchfile.BatchFile.Status;

import lombok.SneakyThrows;

public class FestFileBatch implements Runnable {
    
    private Supplier<MedicationInteractionRepository> repository;
    private UUID uuid;
    private Supplier<BatchFileRepository> batchFileRepository;

    public FestFileBatch(MedicationInteractionRepository repository) {
        this.repository = () -> repository;
    }

    public FestFileBatch(UUID uuid, BatchServerContext applicationContext) {
        this(uuid, applicationContext.batchFileRepository(), applicationContext.medicationInteractionRepository());
    }

    public FestFileBatch(UUID uuid, Supplier<BatchFileRepository> batchFileRepository,
            Supplier<MedicationInteractionRepository> repository) {
        this.uuid = uuid;
        this.batchFileRepository = batchFileRepository;
        this.repository = repository;
    }

    public MedicationInteraction readInteraction(Element oppfWrapper) {
        MedicationInteraction result = new MedicationInteraction();
        result.setId(oppfWrapper.find("Interaksjon", "Id").single().text());
        result.setClinicalConsequence(oppfWrapper.find("Interaksjon", "KliniskKonsekvens").single().text());
        ElementSet mechanism = oppfWrapper.find("Interaksjon", "Interaksjonsmekanisme");
        if (mechanism.isPresent()) {
            result.setInteractionMechanism(mechanism.single().text());
        }
        
        result.getSubstanceCodes().addAll(
                oppfWrapper.find("Interaksjon", "Substansgruppe", "Substans", "Atc").attrs("V"));
        
        return result;
    }
    
    @Override
    @SneakyThrows
    public void run() {
        BatchFile batchFile = batchFileRepository.get().retrieve(uuid);
        processFile(new FileInputStream(batchFile.getFileLocation().toFile()),
                batchFile.getSubmittedFileName());
        batchFile.setStatus(Status.COMPLETE);
        batchFileRepository.get().save(batchFile);
    }

    public void processFile(InputStream inputStream, String filename) throws IOException {
        if (filename.endsWith(".gz")) {
            process(new GZIPInputStream(inputStream));
        } else {
            process(inputStream);
        }
    }

    private void process(InputStream inputStream) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream)) {
            Document document = Xml.read(reader);
            
            for (Element element : document.find("KatInteraksjon", "OppfInteraksjon").check()) {
                repository.get().save(readInteraction(element));
            }
        }
    }

    public void start() {
        BatchFile file = batchFileRepository.get().retrieve(uuid);
        file.setStatus(BatchFile.Status.PROCESSING);
        batchFileRepository.get().save(file);
    }

}
