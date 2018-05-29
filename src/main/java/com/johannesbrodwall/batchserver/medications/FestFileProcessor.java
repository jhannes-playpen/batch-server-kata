package com.johannesbrodwall.batchserver.medications;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.eaxy.Document;
import org.eaxy.Element;
import org.eaxy.ElementSet;
import org.eaxy.Xml;

public class FestFileProcessor {

    public MedicationInteraction readInteraction(Element oppfWrapper) {
        MedicationInteraction result = new MedicationInteraction();
        result.setId(oppfWrapper.find("Interaksjon", "Id").first().text());
        result.setClinicalConsequence(oppfWrapper.find("Interaksjon", "KliniskKonsekvens").first().text());
        result.setInteractionMechanism(oppfWrapper.find("Interaksjon", "Interaksjonsmekanisme").firstTextOrNull());
        
        result.getSubstanceCodes().addAll(
                oppfWrapper.find("Interaksjon", "Substansgruppe", "Substans", "Atc").attrs("V"));
        
        return result;
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
            System.out.println(readInteraction(element));
        }
        
    }

}
