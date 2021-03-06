package uk.nhs.digital.nhsconnect.lab.results.model.edifact;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.message.EdifactValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpecimenCharacteristicFastingStatusTest {

    @Test
    void toEdifactTest() {
        var edifact = new SpecimenCharacteristicFastingStatus("Something").toEdifact();

        assertThat(edifact).isEqualTo("SPC+FS+Something'");
    }

    @Test
    void testFromString() {
        var edifact = "SPC+FS+F'";
        var parsedFreeText = SpecimenCharacteristicFastingStatus.fromString("SPC+FS+F'");
        assertThat(parsedFreeText.getFastingStatus()).isEqualTo("F");
        assertThat(parsedFreeText.toEdifact()).isEqualTo(edifact);
    }

    @Test
    void testFromStringWithInvalidEdifactStringThrowsException() {
        assertThatThrownBy(() -> SpecimenCharacteristicFastingStatus.fromString("wrong value"))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testPreValidationEmptyString() {
        SpecimenCharacteristicFastingStatus emptyFreeText = new SpecimenCharacteristicFastingStatus(StringUtils.EMPTY);
        assertThatThrownBy(emptyFreeText::preValidate)
            .isInstanceOf(EdifactValidationException.class)
            .hasMessage("SPC: Attribute fastingStatus is blank or missing");
    }

    @Test
    void testPreValidationBlankString() {
        SpecimenCharacteristicFastingStatus emptyFreeText = new SpecimenCharacteristicFastingStatus(" ");
        assertThatThrownBy(emptyFreeText::preValidate)
            .isInstanceOf(EdifactValidationException.class)
            .hasMessage("SPC: Attribute fastingStatus is blank or missing");
    }
}
