package com.johannesbrodwall.batchserver.medications;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class MedicationInteraction {

    @Getter @Setter
    private String id;
    
    @Getter
    private final List<String> substanceCodes = new ArrayList<>();
    
    @Getter @Setter
    private String clinicalConsequence, interactionMechanism;
    

}
