package uk.nhs.digital.nhsconnect.lab.results.translator.mapper;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.nhs.digital.nhsconnect.lab.results.fixtures.PathologyRecordFixtures.generatePathologyRecord;

@ExtendWith(MockitoExtension.class)
public class BundleMapperTest {

    @Test
    public void testMapMessageToPathologyRecord() {
        final BundleMapper bundleMapper = new BundleMapper();

        final Bundle bundle = bundleMapper.mapToBundle(generatePathologyRecord());

        assertNotNull(bundle.getMeta().getLastUpdated());
        assertEquals(bundle.getMeta().getProfile().get(0).asStringValue(), "https://fhir.nhs.uk/STU3/StructureDefinition/ITK-Message-Bundle-1");
        assertThat(bundle.getIdentifier().getSystem()).isEqualTo("https://tools.ietf.org/html/rfc4122");
        assertNotNull(bundle.getIdentifier().getValue());
        assertEquals(bundle.getType(), Bundle.BundleType.MESSAGE);

        final int EXPECTED_NUMBER_OF_RESOURCES = 3;
        assertEquals(EXPECTED_NUMBER_OF_RESOURCES, bundle.getEntry().size());

        Bundle.BundleEntryComponent bundleEntryComponentForPerformerResource = bundle.getEntry().get(0);
        assertNotNull(bundleEntryComponentForPerformerResource.getFullUrl());

        Practitioner performer = (Practitioner) bundleEntryComponentForPerformerResource.getResource();
        assertThat(performer.getName())
                .hasSize(1)
                .first()
                .extracting(HumanName::getText)
                .isEqualTo("Dr Claire Hanna");
        assertThat(performer.getGender().toCode())
                .isEqualTo("female");

        Bundle.BundleEntryComponent bundleEntryComponentForRequesterResource = bundle.getEntry().get(1);
        assertNotNull(bundleEntryComponentForPerformerResource.getFullUrl());

        Practitioner requester = (Practitioner) bundleEntryComponentForRequesterResource.getResource();
        assertThat(requester.getName())
                .hasSize(1)
                .first()
                .extracting(HumanName::getText)
                .isEqualTo("Dr Bob Hope");
        assertThat(requester.getGender().toCode())
                .isEqualTo("male");

        Bundle.BundleEntryComponent bundleEntryComponentForSpecimenCollectorResource = bundle.getEntry().get(2);
        assertNotNull(bundleEntryComponentForPerformerResource.getFullUrl());

        Practitioner specimenCollector = (Practitioner) bundleEntryComponentForSpecimenCollectorResource.getResource();
        assertThat(specimenCollector.getName())
                .hasSize(1)
                .first()
                .extracting(HumanName::getText)
                .isEqualTo("Mr Collector");
        assertNull(specimenCollector.getGender());
    }
}
