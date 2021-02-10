package uk.nhs.digital.nhsconnect.lab.results.model.edifact.segmentgroup;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.RangeDetail;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.ReferencePopulationDefinitionFreeText;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SG20 extends SegmentGroup {
    public static final String KEY = "S20";

    // RND+U
    @Getter(lazy = true)
    private final RangeDetail rangeDetail = RangeDetail.fromString(extractSegment(RangeDetail.KEY_QUALIFIER));

    // FTX?
    @Getter
    private final Optional<ReferencePopulationDefinitionFreeText> referencePopulationDefinitionFreeText =
        extractOptionalSegment(ReferencePopulationDefinitionFreeText.KEY_QUALIFIER)
            .map(ReferencePopulationDefinitionFreeText::fromString);

    public static List<SG20> createMultiple(@NonNull final List<String> sg20Segments) {
        return splitMultipleSegmentGroups(sg20Segments, KEY).stream()
            .map(SG20::new)
            .collect(toList());
    }

    public SG20(final List<String> edifactSegments) {
        super(edifactSegments);
    }

    @Override
    public String getKey() {
        return KEY;
    }
}
