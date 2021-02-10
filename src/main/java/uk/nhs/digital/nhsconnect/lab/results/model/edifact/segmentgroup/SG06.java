package uk.nhs.digital.nhsconnect.lab.results.model.edifact.segmentgroup;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.MessageTrailer;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.ReferenceServiceSubject;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.UnstructuredAddress;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SG06 extends SegmentGroup {
    public static final String KEY = "S06";

    // RFF+SSI?
    @Getter(lazy = true)
    private final Optional<ReferenceServiceSubject> referenceServiceSubject =
        extractOptionalSegment(ReferenceServiceSubject.KEY_QUALIFIER)
            .map(ReferenceServiceSubject::fromString);

    // ADR?
    @Getter(lazy = true)
    private final Optional<UnstructuredAddress> unstructuredAddress =
        extractOptionalSegment(UnstructuredAddress.KEY)
            .map(UnstructuredAddress::fromString);

    // S07
    @Getter(lazy = true)
    private final SG07 segmentGroup07 = new SG07(getEdifactSegments().stream()
        .dropWhile(segment -> !segment.startsWith(SG07.KEY))
        .skip(1)
        .takeWhile(segment -> !segment.startsWith(SG16.KEY))
        .takeWhile(segment -> !segment.startsWith(MessageTrailer.KEY))
        .collect(toList()));

    // S016{1,99}
    @Getter(lazy = true)
    private final List<SG16> segmentGroup16s = SG16.createMultiple(getEdifactSegments().stream()
        .dropWhile(segment -> !segment.startsWith(SG16.KEY))
        .takeWhile(segment -> !segment.startsWith(SG18.KEY))
        .takeWhile(segment -> !segment.startsWith(MessageTrailer.KEY))
        .collect(toList()));

    // SG18{1,99}
    @Getter(lazy = true)
    private final List<SG18> segmentGroup18s = SG18.createMultiple(getEdifactSegments().stream()
        .dropWhile(segment -> !segment.startsWith(SG18.KEY))
        .takeWhile(segment -> !segment.startsWith(MessageTrailer.KEY))
        .collect(toList()));

    public SG06(final List<String> edifactSegments) {
        super(edifactSegments);
    }

    @Override
    public String getKey() {
        return KEY;
    }
}
