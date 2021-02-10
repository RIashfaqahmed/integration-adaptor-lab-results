package uk.nhs.digital.nhsconnect.lab.results.model.edifact.segmentgroup;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.ServiceProviderCommentFreeText;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.SpecimenCharacteristicFastingStatus;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.SpecimenCharacteristicType;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.SpecimenCollectionDateTime;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.SpecimenCollectionReceiptDateTime;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.SpecimenQuantity;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.SpecimenReferenceByServiceProvider;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.SpecimenReferenceByServiceRequester;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SG16 extends SegmentGroup {
    public static final String KEY = "S16";

    // SEQ - missing?

    // SPC+TSP
    @Getter(lazy = true)
    private final SpecimenCharacteristicType specimenCharacteristicType =
        SpecimenCharacteristicType.fromString(extractSegment(SpecimenCharacteristicType.KEY_QUALIFIER));

    // SPC+FS?
    @Getter(lazy = true)
    private final Optional<SpecimenCharacteristicFastingStatus> specimenCharacteristicFastingStatus =
        extractOptionalSegment(SpecimenCharacteristicFastingStatus.KEY_QUALIFIER)
            .map(SpecimenCharacteristicFastingStatus::fromString);

    // RFF+RTI?
    @Getter(lazy = true)
    private final Optional<SpecimenReferenceByServiceRequester> specimenReferenceByServiceRequester =
        extractOptionalSegment(SpecimenReferenceByServiceRequester.KEY_QUALIFIER)
            .map(SpecimenReferenceByServiceRequester::fromString);

    // RFF+STI?
    @Getter(lazy = true)
    private final Optional<SpecimenReferenceByServiceProvider> specimenReferenceByServiceProvider =
        extractOptionalSegment(SpecimenReferenceByServiceProvider.KEY_QUALIFIER)
            .map(SpecimenReferenceByServiceProvider::fromString);

    // QTY?
    @Getter(lazy = true)
    private final Optional<SpecimenQuantity> specimenQuantity =
        extractOptionalSegment(SpecimenQuantity.KEY_QUALIFIER)
            .map(SpecimenQuantity::fromString);

    // DTM+SCO?
    @Getter(lazy = true)
    private final Optional<SpecimenCollectionDateTime> specimenCollectionDateTime =
        extractOptionalSegment(SpecimenCollectionDateTime.KEY_QUALIFIER)
            .map(SpecimenCollectionDateTime::fromString);

    // DTM+SRI?
    @Getter(lazy = true)
    private final Optional<SpecimenCollectionReceiptDateTime> specimenCollectionReceiptDateTime =
        extractOptionalSegment(SpecimenCollectionReceiptDateTime.KEY_QUALIFIER)
            .map(SpecimenCollectionReceiptDateTime::fromString);

    // FTX{,9}
    @Getter(lazy = true)
    private final List<ServiceProviderCommentFreeText> serviceProviderCommentFreeTexts =
        extractSegments(ServiceProviderCommentFreeText.KEY_QUALIFIER).stream()
            .map(ServiceProviderCommentFreeText::fromString)
            .collect(Collectors.toList());

    public static List<SG16> createMultiple(@NonNull final List<String> sg16Segments) {
        return splitMultipleSegmentGroups(sg16Segments, KEY).stream()
            .map(SG16::new)
            .collect(Collectors.toList());
    }

    public SG16(final List<String> edifactSegments) {
        super(edifactSegments);
    }

    @Override
    public String getKey() {
        return KEY;
    }
}
