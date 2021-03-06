package uk.nhs.digital.nhsconnect.lab.results.model.edifact;

import org.junit.jupiter.api.Test;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.message.EdifactValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GpNameAndAddressTest {
    private final GpNameAndAddress gpNameAndAddress = new GpNameAndAddress("ABC", "code1");

    @Test
    void testGetKey() {
        assertThat(gpNameAndAddress.getKey()).isEqualTo("NAD");
    }

    @Test
    void testGetValue() {
        assertThat(gpNameAndAddress.getValue()).isEqualTo("GP+ABC:code1");
    }

    @Test
    void testValidateStateful() {
        assertThatCode(gpNameAndAddress::validateStateful).doesNotThrowAnyException();
    }

    @Test
    void testPreValidateEmptyIdentifier() {
        final GpNameAndAddress emptyIdentifier = new GpNameAndAddress("", "x");
        assertThatThrownBy(emptyIdentifier::preValidate)
            .isExactlyInstanceOf(EdifactValidationException.class)
            .hasMessage("NAD: Attribute identifier is required");
    }

    @Test
    void testPreValidateEmptyCode() {
        final GpNameAndAddress emptyCode = new GpNameAndAddress("x", "");
        assertThatThrownBy(emptyCode::preValidate)
            .isExactlyInstanceOf(EdifactValidationException.class)
            .hasMessage("NAD: Attribute code is required");
    }

    @Test
    void testFromString() {
        assertThat(GpNameAndAddress.fromString("NAD+GP+ABC:code1").getValue()).isEqualTo(gpNameAndAddress.getValue());
        assertThatThrownBy(() -> GpNameAndAddress.fromString("wrong value"))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_mappingToEdifact_expect_returnCorrectString() {
        final var personGP = GpNameAndAddress.builder()
            .identifier("4826940,281")
            .code("900")
            .build();

        assertEquals("NAD+GP+4826940,281:900'", personGP.toEdifact());
    }

    @Test
    void when_mappingToEdifactWithEmptyMandatoryFields_expect_edifactValidationExceptionIsThrown() {
        var personGP = GpNameAndAddress.builder()
            .identifier("")
            .code("")
            .build();

        assertThrows(EdifactValidationException.class, personGP::toEdifact);
    }

    @Test
    void when_buildingWithoutMandatoryFields_expect_nullPointerExceptionIsThrown() {
        assertThrows(NullPointerException.class, () -> GpNameAndAddress.builder().build());
    }
}
