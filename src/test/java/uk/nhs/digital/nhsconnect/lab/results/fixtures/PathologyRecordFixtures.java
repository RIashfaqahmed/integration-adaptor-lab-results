package uk.nhs.digital.nhsconnect.lab.results.fixtures;

import uk.nhs.digital.nhsconnect.lab.results.model.fhir.PathologyRecord;

import java.util.List;

import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generatePatient;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generatePerformer;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generatePerformingOrganisation;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generateRequester;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generateRequestingOrganization;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generateSpecimen;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generateSpecimenCollectingOrganization;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generateSpecimenCollector;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generateTestGroup;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generateTestReport;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generateTestRequestSummary;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.FhirFixtures.generateTestResult;

public final class PathologyRecordFixtures {

    private PathologyRecordFixtures() { }

    public static PathologyRecord generatePathologyRecord() {
        return PathologyRecord.builder()
                .patient(generatePatient())
                .performers(List.of(generatePerformer()))
                .performingOrganization(generatePerformingOrganisation())
                .requesters(List.of(generateRequester()))
                .requestingOrganization(generateRequestingOrganization())
                .specimenCollectingOrganization(generateSpecimenCollectingOrganization())
                .specimenCollectors(List.of(generateSpecimenCollector()))
                .specimens(List.of(generateSpecimen()))
                .testGroups(List.of(generateTestGroup()))
                .testReports(List.of(generateTestReport()))
                .testRequestSummary(generateTestRequestSummary())
                .testResults(List.of(generateTestResult()))
                .build();
    }
}
