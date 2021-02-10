package uk.nhs.digital.nhsconnect.lab.results.fixtures;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.ProcedureRequest;
import org.hl7.fhir.dstu3.model.Specimen;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public final class FhirFixtures {

    private FhirFixtures() { }

    public static Patient generatePatient() {
        Patient patient = new Patient();

        patient.addName().setText("Mr Adam Smith");
        patient.setGender(Enumerations.AdministrativeGender.MALE);

        Address address = new Address();
        address.addLine("1 Old Road")
            .addLine("London");
        address.setPostalCode("ABC 123");

        patient.setAddress(List.of(address));

        return patient;
    }

    public static Practitioner generatePerformer() {
        Practitioner performer = new Practitioner();

        performer.addName().setText("Dr Claire Hanna");
        performer.setGender(Enumerations.AdministrativeGender.FEMALE);

        return performer;
    }

    public static Organization generatePerformingOrganisation() {
        Organization performingOrganization = new Organization();

        performingOrganization.setName("Some hospital");

        Address address = new Address();
        address.addLine("10 Long Avenue")
                .addLine("London");
        address.setPostalCode("ZXC456 9SH");

        performingOrganization.setAddress(List.of(address));

        Coding coding = new Coding().setCode("dept").setDisplay("Haematology");
        CodeableConcept codeableConcept = new CodeableConcept().setCoding(List.of(coding));

        performingOrganization.setType(List.of(codeableConcept));

        return performingOrganization;
    }

    public static Practitioner generateRequester() {
        Practitioner requester = new Practitioner();

        requester.addName().setText("Dr Bob Hope");
        requester.setGender(Enumerations.AdministrativeGender.MALE);

        return requester;
    }

    public static Organization generateRequestingOrganization() {
        Organization requestingOrganization = new Organization();

        requestingOrganization.setName("Some GP");

        Address address = new Address();
        address.addLine("5 New Street")
                .addLine("London");
        address.setPostalCode("IOP 789");

        requestingOrganization.setAddress(List.of(address));

        return requestingOrganization;
    }

    public static Organization generateSpecimenCollectingOrganization() {
        Organization specimenCollectingOrganization = new Organization();

        specimenCollectingOrganization.setName("Some specimen collecting organization");

        Address address = new Address();
        address.addLine("20 Red Road")
                .addLine("London");
        address.setPostalCode("RTY 222");

        specimenCollectingOrganization.setAddress(List.of(address));

        return specimenCollectingOrganization;
    }

    public static Practitioner generateSpecimenCollector() {
        Practitioner specimenCollector = new Practitioner();

        specimenCollector.addName().setText("Mr Collector");

        return specimenCollector;
    }

    public static Specimen generateSpecimen() {
        Specimen specimen = new Specimen();

        Identifier identifier = new Identifier();
        identifier.setValue("CH000037KA");

        specimen.setIdentifier(List.of(identifier));

        specimen.setStatus(Specimen.SpecimenStatus.AVAILABLE);

        Coding coding = new Coding().setDisplay("VENOUS BLOOD");
        CodeableConcept codeableConcept = new CodeableConcept().setCoding(List.of(coding));

        specimen.setType(codeableConcept);

        final int YEAR = 2010;
        final int DAY_OF_MONTH = 24;
        final int HOUR_OF_DAY = 15;
        final int MINUTE = 41;
        specimen.setReceivedTime(new GregorianCalendar(YEAR, Calendar.FEBRUARY, DAY_OF_MONTH, HOUR_OF_DAY, MINUTE).getTime());

        return specimen;
    }

    public static Observation generateTestGroup() {
        Observation testGroup = new Observation();

        Identifier identifier = new Identifier();
        identifier.setValue("TESTGROUP1");

        testGroup.setIdentifier(List.of(identifier));

        return testGroup;
    }

    public static DiagnosticReport generateTestReport() {
        DiagnosticReport testReport = new DiagnosticReport();

        Identifier identifier = new Identifier();
        identifier.setValue("TESTREPORT1");

        testReport.setIdentifier(List.of(identifier));

        return testReport;
    }

    public static ProcedureRequest generateTestRequestSummary() {
        ProcedureRequest testRequestSummary = new ProcedureRequest();

        Identifier identifier = new Identifier();
        identifier.setValue("TESTREQUESTSUMMARY");

        testRequestSummary.setIdentifier(List.of(identifier));

        return testRequestSummary;
    }

    public static Observation generateTestResult() {
        Observation testResult = new Observation();

        Identifier identifier = new Identifier();
        identifier.setValue("TESTRESULT1");

        testResult.setIdentifier(List.of(identifier));

        return testResult;
    }
}
