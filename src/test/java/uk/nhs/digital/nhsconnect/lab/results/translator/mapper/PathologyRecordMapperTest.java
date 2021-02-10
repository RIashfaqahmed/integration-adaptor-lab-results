package uk.nhs.digital.nhsconnect.lab.results.translator.mapper;

import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.Message;
import uk.nhs.digital.nhsconnect.lab.results.model.fhir.PathologyRecord;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generatePerformer;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generateRequester;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generateSpecimenCollector;

@ExtendWith(MockitoExtension.class)
public class PathologyRecordMapperTest {

    @Mock
    private DiagnosticReportMapper diagnosticReportMapper;

    @Mock
    private ObservationMapper observationMapper;

    @Mock
    private OrganizationMapper organizationMapper;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private PractitionerMapper practitionerMapper;

    @Mock
    private ProcedureRequestMapper procedureRequestMapper;

    @Mock
    private SpecimenMapper specimenMapper;

    @InjectMocks
    private PathologyRecordMapper pathologyRecordMapper;

    @Test
    public void testMapMessageToPathologyRecord() {
        final Message message = new Message(new ArrayList<>());

        when(practitionerMapper.mapToPerformers(message)).thenReturn(List.of(generatePerformer()));
        when(practitionerMapper.mapToRequesters(message)).thenReturn(List.of(generateRequester()));
        when(practitionerMapper.mapToSpecimenCollectors(message)).thenReturn(List.of(generateSpecimenCollector()));

        final PathologyRecord pathologyRecord = pathologyRecordMapper.mapToPathologyRecord(message);

        Practitioner performer = pathologyRecord.getPerformers().get(0);
        assertThat(performer.getName())
                .hasSize(1)
                .first()
                .extracting(HumanName::getText)
                .isEqualTo("Dr Claire Hanna");
        assertThat(performer.getGender().toCode())
                .isEqualTo("female");

        Practitioner requester = pathologyRecord.getRequesters().get(0);
        assertThat(requester.getName())
                .hasSize(1)
                .first()
                .extracting(HumanName::getText)
                .isEqualTo("Dr Bob Hope");
        assertThat(requester.getGender().toCode())
                .isEqualTo("male");

        Practitioner specimenCollector = pathologyRecord.getSpecimenCollectors().get(0);
        assertThat(specimenCollector.getName())
                .hasSize(1)
                .first()
                .extracting(HumanName::getText)
                .isEqualTo("Mr Collector");
        assertNull(specimenCollector.getGender());
    }
}
