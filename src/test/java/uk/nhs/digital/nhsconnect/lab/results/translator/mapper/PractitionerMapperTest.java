package uk.nhs.digital.nhsconnect.lab.results.translator.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.Message;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.RequesterNameAndAddress;

@ExtendWith(MockitoExtension.class)
class PractitionerMapperTest {

    @Mock
    private Message message;

    @Mock
    private RequesterNameAndAddress requester;

    private PractitionerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PractitionerMapper();
    }

    @Test
    void testMapMessageToPractitionerNoRequester() {
        when(message.getRequesterNameAndAddress()).thenReturn(Optional.empty());

        assertThat(mapper.mapRequester(message)).isEmpty();
    }

    @Test
    void testMapMessageToPractitionerWithRequester() {
        when(message.getRequesterNameAndAddress()).thenReturn(Optional.of(requester));
        when(requester.getRequesterName()).thenReturn("Alan Turing");
        when(requester.getIdentifier()).thenReturn("Identifier");

        Optional<Practitioner> result = mapper.mapRequester(message);
        assertThat(result).isNotEmpty();

        Practitioner practitioner = result.get();
        assertThat(practitioner.getName())
            .hasSize(1)
            .first()
            .extracting(HumanName::getText)
            .isEqualTo("Alan Turing");
        assertThat(practitioner.getIdentifier())
            .hasSize(1)
            .first()
            .satisfies(identifier -> assertAll(
                () -> assertThat(identifier.getValue()).isEqualTo("Identifier"),
                () -> assertThat(identifier.getSystem()).isEqualTo("https://fhir.nhs.uk/Id/sds-user-id")
            ));
    }

    @Test
    void testMapMessageToPractitionerWithUnnamedRequester() {
        when(message.getRequesterNameAndAddress()).thenReturn(Optional.of(requester));

        Optional<Practitioner> result = mapper.mapRequester(message);
        assertThat(result).isNotEmpty();

        Practitioner practitioner = result.get();
        assertThat(practitioner.getName()).isEmpty();
    }
}
