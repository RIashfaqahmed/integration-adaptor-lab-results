package uk.nhs.digital.nhsconnect.lab.results.model.fhir;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.ProcedureRequest;
import org.hl7.fhir.dstu3.model.Specimen;

import java.util.List;

@Builder
@Getter
@Setter
public class PathologyRecord {
    private Patient patient;
    private List<Practitioner> performers;
    private Organization performingOrganization;
    private List<Practitioner> requesters;
    private Organization requestingOrganization;
    private Organization specimenCollectingOrganization;
    private List<Practitioner> specimenCollectors;
    private List<Specimen> specimens;
    private List<Observation> testGroups;
    private List<DiagnosticReport> testReports;
    private ProcedureRequest testRequestSummary;
    private List<Observation> testResults;
}
