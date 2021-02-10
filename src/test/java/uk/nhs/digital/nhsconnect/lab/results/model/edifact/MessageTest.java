package uk.nhs.digital.nhsconnect.lab.results.model.edifact;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class MessageTest {
    @Test
    void testGetMessageHeader() {
        final Message msg = new Message(List.of(
            "UNH+00000009+FHSREG:0:1:FH:FHS001"
        ));

        MessageHeader header = msg.getMessageHeader();

        assertAll(
            () -> assertEquals("UNH", header.getKey()),
            () -> assertEquals("00000009+FHSREG:0:1:FH:FHS001", header.getValue())
        );
    }

    @Test
    void testGetHealthAuthorityNameAndAddress() {
        final Message msg = new Message(List.of(
            "NAD+FHS+XX1:954"
        ));

        HealthAuthorityNameAndAddress haAddress = msg.getHealthAuthorityNameAndAddress();

        assertAll(
            () -> assertEquals("NAD", haAddress.getKey()),
            () -> assertEquals("FHS+XX1:954", haAddress.getValue()),
            () -> assertEquals("954", haAddress.getCode()),
            () -> assertEquals("XX1", haAddress.getIdentifier())
        );
    }

    @Test
    void testFindFirstGpCodeDefaultsTo9999() {
        final Message msg = new Message(List.of());

        String firstGpCode = msg.findFirstGpCode();

        assertEquals("9999", firstGpCode);
    }

    @Test
    void testFindFirstGpCodeReturnsCorrectly() {
        final Message msg = new Message(List.of(
            "NAD+GP+2750922,295:900",
            "NAD+GP+1649811,184:899"
        ));

        String firstGpCode = msg.findFirstGpCode();

        assertEquals("2750922,295", firstGpCode);
    }

    @Test
    void testSegmentGroups() {
        final Message msg = new Message(List.of(
            "UNH+1+MEDRPT:0:1:RT:NHS003",
            "BGM+LSR",
            "DTM+137:201002261541:203",
            "NAD+FHS+XX1:954",

            "S01+01",
            "NAD+PO+G3380314:900++SCOTT",
            "SPR+PRO",
            "S01+01",
            "NAD+MR+G3380314:900++SCOTT", // we don't have anything for NAD+MR
            "SPR+PRO",
            "S01+01",
            "NAD+MR+D82015:901", // we don't have anything for NAD+MR
            "SPR+ORG",
            "S01+01",
            "NAD+SLA+++Haematology",
            "SPR+DPT",
            "S01+01",
            "NAD+SLA+++ST JAMES?'S UNIVERSITY HOSPITAL",
            "SPR+ORG",

            "S02+02",
            "GIS+N",
            "RFF+SRI:15/CH000037K/200010191704",
            "STS++UN",
            "DTM+ISR:201002251541:203",
            "S06+06",
            "ADR++US:FLAT1:59 SANDTOFT ROAD:BELTON:DONCASTER:++DN9 1PJ",
            "S07+07",
            "PNA+PAT+9435492908:OPI+++SU:AZIZ+FO:NISMA",
            "DTM+329:19450730:102",
            "PDI+2",
            "S10+10",
            "CIN+UN",
            "FTX+CID+++TIRED ALL THE TIME, LOW Hb",
            "S16+16",
            "SEQ++1",
            "SPC+TSP+:::VENOUS BLOOD",
            "RFF+STI:CH000037KA",
            "DTM+SCO:201002231541:203",
            "DTM+SRI:201002241541:203",
            "S18+18",
            "GIS+N",
            "INV+MQ+42R4.:911::Serum ferritin",
            "RSL+NV+11++:::ng/mL",
            "RFF+ASL:1",
            "S20+20",
            "RND+U+10+200",
            "FTX+RPD+++Upper age - 45 years:Sex - Female",
            "UNT+46+1"
        ));

        assertAll("Message",
            () -> assertEquals("9999", msg.findFirstGpCode()),
            () -> assertEquals(1, msg.getMessageHeader().getSequenceNumber()),
            () -> assertAll("HealthAuthorityNameAndAddress",
                () -> assertEquals("XX1", msg.getHealthAuthorityNameAndAddress().getIdentifier()),
                () -> assertEquals("954", msg.getHealthAuthorityNameAndAddress().getCode())),
            () -> {
                final var sg01s = msg.getSegmentGroup01s();
                assertAll("Segment Group 1",
                    () -> assertEquals(5, sg01s.size()),
                    () -> {
                        final var it = sg01s.iterator();
                        final var sg01 = new AtomicReference<>(it.next());
                        assertAll("#1",
                            () -> assertEquals(ServiceProviderCode.PROFESSIONAL,
                                sg01.get().getServiceProvider().getServiceProviderCode()),
                            () -> assertTrue(sg01.get().getPerformingOrganisationNameAndAddress().isEmpty()),
                            () -> assertEquals("SCOTT", sg01.get().getRequesterNameAndAddress()
                                .map(RequesterNameAndAddress::getRequesterName).orElse(null)),
                            () -> assertEquals("G3380314", sg01.get().getRequesterNameAndAddress()
                                .map(RequesterNameAndAddress::getIdentifier).orElse(null)),
                            () -> assertEquals("900", sg01.get().getRequesterNameAndAddress()
                                .map(RequesterNameAndAddress::getHealthcareRegistrationIdentificationCode)
                                .map(HealthcareRegistrationIdentificationCode::getCode).orElse(null)));

                        sg01.set(it.next());
                        assertAll("#2",
                            () -> assertEquals(ServiceProviderCode.PROFESSIONAL,
                                sg01.get().getServiceProvider().getServiceProviderCode()),
                            () -> assertTrue(sg01.get().getRequesterNameAndAddress().isEmpty()),
                            () -> assertTrue(sg01.get().getPerformingOrganisationNameAndAddress().isEmpty()));

                        sg01.set(it.next());
                        assertAll("#3",
                            () -> assertEquals(ServiceProviderCode.ORGANISATION,
                                sg01.get().getServiceProvider().getServiceProviderCode()),
                            () -> assertTrue(sg01.get().getRequesterNameAndAddress().isEmpty()),
                            () -> assertTrue(sg01.get().getPerformingOrganisationNameAndAddress().isEmpty()));

                        sg01.set(it.next());
                        assertAll("#4",
                            () -> assertEquals(ServiceProviderCode.DEPARTMENT,
                                sg01.get().getServiceProvider().getServiceProviderCode()),
                            () -> assertTrue(sg01.get().getRequesterNameAndAddress().isEmpty()),
                            () -> assertEquals("Haematology", sg01.get().getPerformingOrganisationNameAndAddress()
                                .map(PerformingOrganisationNameAndAddress::getPerformingOrganisationName)
                                .orElse(null)));

                        sg01.set(it.next());
                        assertAll("#5",
                            () -> assertEquals(ServiceProviderCode.ORGANISATION,
                                sg01.get().getServiceProvider().getServiceProviderCode()),
                            () -> assertTrue(sg01.get().getRequesterNameAndAddress().isEmpty()),
                            () -> assertEquals("ST JAMES?'S UNIVERSITY HOSPITAL",
                                sg01.get().getPerformingOrganisationNameAndAddress()
                                    .map(PerformingOrganisationNameAndAddress::getPerformingOrganisationName)
                                    .orElse(null)));
                    });
            },
            () -> {
                final var sg02 = msg.getSegmentGroup02();
                assertAll("Segment Group 2",
                    () -> assertEquals("N", sg02.getDiagnosticReportCode().getCode()),
                    () -> assertEquals("15/CH000037K/200010191704",
                        sg02.getReferenceDiagnosticReport().getReferenceNumber()),
                    () -> assertEquals("", sg02.getDiagnosticReportStatus().getDetail()),
                    () -> assertEquals(LocalDateTime.of(2010, 2, 25, 15, 41),
                        sg02.getDiagnosticReportDateIssued().getDateIssued()),
                    () -> {
                        final var sg06 = sg02.getSegmentGroup06();
                        assertAll("Segment Group 6",
                            () -> assertTrue(sg06.getReferenceServiceSubject().isEmpty()),
                            () -> {
                                final var address = sg06.getUnstructuredAddress().orElseThrow();
                                assertAll("S06 > ADR",
                                    () -> assertEquals("US", address.getFormat()),
                                    () -> assertArrayEquals(new String[] {
                                        "FLAT1", "59 SANDTOFT ROAD", "BELTON", "DONCASTER", ""
                                    }, address.getAddressLines()),
                                    () -> assertEquals("DN9 1PJ", address.getPostCode()));
                            },
                            () -> {
                                final var sg07 = sg06.getSegmentGroup07();
                                final var name = sg07.getPersonName();
                                final var dob = sg07.getPersonDateOfBirth().orElseThrow();
                                final var sex = sg07.getPersonSex().orElseThrow();
                                assertAll("Segment Group 7",
                                    () -> assertEquals("9435492908", name.getNhsNumber()),
                                    () -> assertEquals(PatientIdentificationType.OFFICIAL_PATIENT_IDENTIFICATION,
                                        name.getPatientIdentificationType()),
                                    () -> assertEquals("AZIZ", name.getSurname()),
                                    () -> assertEquals("NISMA", name.getFirstForename()),
                                    () -> assertNull(name.getTitle()),
                                    () -> assertNull(name.getSecondForename()),

                                    () -> assertEquals("102", dob.getDateFormat().getCode()),
                                    () -> assertEquals("1945-07-30", dob.getDateOfBirth()),

                                    () -> assertEquals("2", sex.getGender().getCode()));
                            },
                            () -> {
                                final var segmentGroup16s = sg06.getSegmentGroup16s();
                                assertAll("Segment Group 16",
                                    () -> assertEquals(1, segmentGroup16s.size()),
                                    () -> {
                                        final var sg16 = segmentGroup16s.get(0);
                                        assertAll("#1",
                                            () -> assertEquals("VENOUS BLOOD",
                                                sg16.getSpecimenCharacteristicType().getTypeOfSpecimen()),
                                            () -> assertNull(sg16.getSpecimenCharacteristicFastingStatus()
                                                .map(SpecimenCharacteristicFastingStatus::getFastingStatus)
                                                .orElse(null)),
                                            () -> assertNull(sg16.getSpecimenReferenceByServiceRequester()
                                                .map(SpecimenReferenceByServiceRequester::getReferenceNumber)
                                                .orElse(null)),
                                            () -> assertEquals("CH000037KA",
                                                sg16.getSpecimenReferenceByServiceProvider()
                                                    .map(SpecimenReferenceByServiceProvider::getReferenceNumber)
                                                    .orElse(null)),
                                            () -> assertTrue(sg16.getSpecimenQuantity().isEmpty()),
                                            () -> assertEquals("203", sg16.getSpecimenCollectionDateTime()
                                                .map(SpecimenCollectionDateTime::getDateFormat)
                                                .map(DateFormat::getCode).orElse(null)),
                                            () -> assertEquals("2010-02-23T15:41+00:00",
                                                sg16.getSpecimenCollectionDateTime()
                                                    .map(SpecimenCollectionDateTime::getCollectionDateTime)
                                                    .orElse(null)),
                                            () -> assertEquals("203", sg16.getSpecimenCollectionReceiptDateTime()
                                                .map(SpecimenCollectionReceiptDateTime::getDateFormat)
                                                .map(DateFormat::getCode).orElse(null)),
                                            () -> assertEquals("2010-02-24T15:41+00:00",
                                                sg16.getSpecimenCollectionReceiptDateTime()
                                                    .map(
                                                        SpecimenCollectionReceiptDateTime::getCollectionReceiptDateTime)
                                                    .orElse(null)),
                                            () -> assertTrue(sg16.getServiceProviderCommentFreeTexts().isEmpty()));
                                    });
                            },
                            () -> {
                                final var segmentGroup18s = sg06.getSegmentGroup18s();
                                assertAll("Segment Group 18",
                                    () -> assertEquals(1, segmentGroup18s.size()),
                                    () -> {
                                        final var sg18 = segmentGroup18s.get(0);
                                        final var labResult = sg18.getLaboratoryInvestigationResult().orElseThrow();
                                        assertAll("#1",
                                            () -> assertEquals("N", sg18.getDiagnosticReportCode().getCode()),
                                            () -> assertEquals("42R4.",
                                                sg18.getLaboratoryInvestigation().getInvestigationCode()),
                                            () -> assertEquals("Serum ferritin",
                                                sg18.getLaboratoryInvestigation().getInvestigationDescription()),

                                            () -> assertEquals(BigDecimal.valueOf(11), labResult.getMeasurementValue()),
                                            () -> assertNull(labResult.getMeasurementValueComparator()),
                                            () -> assertEquals("ng/mL", labResult.getMeasurementUnit()),
                                            () -> assertNull(labResult.getDeviatingResultIndicator()),

                                            () -> assertTrue(sg18.getTestStatus().isEmpty()),
                                            () -> assertTrue(sg18.getServiceProviderCommentFreeTexts().isEmpty()),

                                            () -> {
                                                final var segmentGroup20s = sg18.getSegmentGroup20s();
                                                assertAll("Segment Group 20",
                                                    () -> assertEquals(1, segmentGroup20s.size()),
                                                    () -> {
                                                        final var sg20 = segmentGroup20s.get(0);
                                                        assertAll("#1",
                                                            () -> assertEquals(BigDecimal.TEN,
                                                                sg20.getRangeDetail().getLowerLimit()),
                                                            () -> assertEquals(BigDecimal.valueOf(200),
                                                                sg20.getRangeDetail().getUpperLimit()),
                                                            () -> assertArrayEquals(new String[] {
                                                                    "Upper age - 45 years", "Sex - Female"
                                                                },
                                                                sg20.getReferencePopulationDefinitionFreeText()
                                                                    .map(
                                                                        ReferencePopulationDefinitionFreeText::getFreeTexts)
                                                                    .orElse(null)));
                                                    });
                                            });
                                    });
                            });
                    });
            });
    }
}
