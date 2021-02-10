package uk.nhs.digital.nhsconnect.lab.results.translator.mapper;

import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.UriType;
import org.springframework.stereotype.Component;
import uk.nhs.digital.nhsconnect.lab.results.model.fhir.PathologyRecord;

import java.util.List;

@Component
@Slf4j
public class BundleMapper {
    private static final List<UriType> BUNDLE_META_PROFILE = List.of(new UriType("https://fhir.nhs.uk/STU3/StructureDefinition/ITK-Message-Bundle-1"));
    private static final String BUNDLE_IDENTIFIER_SYSTEM = "https://tools.ietf.org/html/rfc4122";

    public Bundle mapToBundle(PathologyRecord pathologyRecord) {
        Bundle bundle = generateInitialPathologyBundle();

        // TODO: fullUrl is optional. But what is the fullUrl property on the entry? E.g., "urn:uuid:a4409d7c-b613-477c-b623-87e60406c2f0"
        pathologyRecord.getPerformers().forEach(
            performer -> bundle.addEntry().setFullUrl("urn:uuid:some-unique-uuid").setResource(performer)
        );

        pathologyRecord.getRequesters().forEach(
            requester -> bundle.addEntry().setFullUrl("urn:uuid:some-unique-uuid").setResource(requester)
        );

        pathologyRecord.getSpecimenCollectors().forEach(
            specimenCollector -> bundle.addEntry().setFullUrl("urn:uuid:some-unique-uuid").setResource(specimenCollector)
        );

        return bundle;
    }

    private Bundle generateInitialPathologyBundle() {
        Bundle bundle = new Bundle();

        bundle.setMeta(new Meta()
                .setLastUpdatedElement((InstantType.now()))
                .setProfile(BUNDLE_META_PROFILE)
        );
        bundle.setIdentifier(new Identifier()
                .setSystem(BUNDLE_IDENTIFIER_SYSTEM)
                // TODO: value is optional. But what is the value property on the bundle? E.g., "f36927ef-7703-45ed-b0e5-6ec6723cf0f6"
                .setValue("some-unique-uuid")
        );
        bundle.setType(Bundle.BundleType.MESSAGE);

        return bundle;
    }
}
