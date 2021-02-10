package uk.nhs.digital.nhsconnect.lab.results.translator.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.Message;
import uk.nhs.digital.nhsconnect.lab.results.model.fhir.PathologyRecord;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class PathologyRecordMapper {

    private final DiagnosticReportMapper diagnosticReportMapper;
    private final ObservationMapper observationMapper;
    private final OrganizationMapper organizationMapper;
    private final PatientMapper patientMapper;
    private final PractitionerMapper practitionerMapper;
    private final ProcedureRequestMapper procedureRequestMapper;
    private final SpecimenMapper specimenMapper;

    public PathologyRecord mapToPathologyRecord(Message message) {
        return PathologyRecord.builder()
                .patient(patientMapper.mapToPatient(message))
                .performers(practitionerMapper.mapToPerformers(message))
                .performingOrganization(organizationMapper.mapToPerformingOrganization(message))
                .requesters(practitionerMapper.mapToRequesters(message))
                .requestingOrganization(organizationMapper.mapToRequestingOrganization(message))
                .specimenCollectingOrganization(organizationMapper.mapToSpecimenCollectingOrganization(message))
                .specimenCollectors(practitionerMapper.mapToSpecimenCollectors(message))
                .specimens(specimenMapper.mapToSpecimens(message))
                .testGroups(observationMapper.mapToTestGroups(message))
                .testReports(diagnosticReportMapper.map(message))
                .testRequestSummary(procedureRequestMapper.mapToProcedureRequest(message))
                .testResults(observationMapper.mapToTestResults(message))
                .build();
    }
}
