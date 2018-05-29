package com.johannesbrodwall.batchserver.medications;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Random;

import org.eaxy.Element;
import org.eaxy.Namespace;
import org.eaxy.Validator;
import org.eaxy.Xml;
import org.junit.Test;


public class FestFileProcessorTest {
    
    private static final Namespace M30 = new Namespace("http://www.kith.no/xmlstds/eresept/m30/2014-12-01", "m30");
    private Validator validator = Xml.validatorFromResource("R1808-eResept-M30-2014-12-01/ER-M30-2014-12-01.xsd");

    @Test
    public void shouldReadInteractions() {
        Element interaksjon = M30.el("Interaksjon",
                M30.el("Id", "ID_06688DFC-BF07-4113-A6E4-9F8F00E5A536"),
                M30.el("KliniskKonsekvens", "Risiko for toksiske..."),
                M30.el("Interaksjonsmekanisme", "Metylfenidat frigjør..."),
                M30.el("Substansgruppe",
                        M30.el("Substans",
                                M30.el("Substans", "Metylfenidat"),
                                M30.el("Atc").attr("V", "N06BA05").attr("DN", "Metylfenidat"))),
                M30.el("Substansgruppe",
                        M30.el("Substans",
                                M30.el("Substans", "Moklobemid"),
                                M30.el("Atc").attr("V", "N06AG02").attr("DN", "Moklobemid")))
                );
        validator.validate(katWrapper(interaksjon));

        MedicationInteraction interaction = new FestFileProcessor()
                .readInteraction(oppfWrapper(interaksjon));

        assertThat(interaction).hasNoNullFieldsOrProperties();
        assertThat(interaction.getSubstanceCodes()).contains("N06BA05", "N06AG02");
        assertThat(interaction.getId()).isEqualTo("ID_06688DFC-BF07-4113-A6E4-9F8F00E5A536");
        assertThat(interaction.getClinicalConsequence()).isEqualTo("Risiko for toksiske...");
        assertThat(interaction.getInteractionMechanism()).isEqualTo("Metylfenidat frigjør...");
    }


    private Element katWrapper(Element el) {
        return M30.el("Kat" + el.tagName(), oppfWrapper(el));
    }

    private Element oppfWrapper(Element el) {
        return M30.el("Oppf" + el.tagName(),
                M30.el("Id", "ID_" + random.nextInt()),
                M30.el("Tidspunkt", Instant.now().toString()),
                M30.el("Status", ""),
                el);
    }
    
    private Random random = new Random();
}
