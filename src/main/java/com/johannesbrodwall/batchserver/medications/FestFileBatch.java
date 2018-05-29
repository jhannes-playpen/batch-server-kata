package com.johannesbrodwall.batchserver.medications;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

import org.eaxy.Document;
import org.eaxy.Element;
import org.eaxy.Xml;

import com.johannesbrodwall.batchserver.BatchServerContext;
import com.johannesbrodwall.batchserver.batchfile.BatchFile;
import com.johannesbrodwall.batchserver.batchfile.BatchFileRepository;

import lombok.SneakyThrows;

public class FestFileBatch implements Runnable {
    
    private MedicationInteractionRepository repository;
    private UUID uuid;
    private Supplier<BatchFileRepository> batchFileRepository;

    public FestFileBatch(MedicationInteractionRepository repository) {
        this.repository = repository;
    }

    public FestFileBatch(UUID uuid, BatchServerContext applicationContext) {
        this.uuid = uuid;
        this.repository = applicationContext.medicationInteractionRepository().get();
        this.batchFileRepository = applicationContext.batchFileRepository();
    }

    public MedicationInteraction readInteraction(Element oppfWrapper) {
        MedicationInteraction result = new MedicationInteraction();
        result.setId(oppfWrapper.find("Interaksjon", "Id").first().text());
        result.setClinicalConsequence(oppfWrapper.find("Interaksjon", "KliniskKonsekvens").first().text());
        result.setInteractionMechanism(oppfWrapper.find("Interaksjon", "Interaksjonsmekanisme").firstTextOrNull());
        
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
    }

    public void processFile(InputStream inputStream, String filename) throws IOException {
        if (filename.endsWith(".gz")) {
            process(new GZIPInputStream(inputStream));
        } else {
            process(inputStream);
        }
    }

    private void process(InputStream inputStream) throws IOException {
        Document document = Xml.readAndClose(inputStream);
        
        for (Element element : document.find("KatInteraksjon", "OppfInteraksjon").check()) {
            repository.save(readInteraction(element));
        }
    }

}
