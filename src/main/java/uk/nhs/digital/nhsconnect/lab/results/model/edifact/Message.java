package uk.nhs.digital.nhsconnect.lab.results.model.edifact;

import static java.util.stream.Collectors.toList;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.segmentgroup.SG01;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.segmentgroup.SG02;

public class Message extends Section {
    private static final String DEFAULT_GP_CODE = "9999";

    @Getter(lazy = true)
    private final MessageHeader messageHeader =
        MessageHeader.fromString(extractSegment(MessageHeader.KEY));

    @Getter(lazy = true)
    private final HealthAuthorityNameAndAddress healthAuthorityNameAndAddress =
        HealthAuthorityNameAndAddress.fromString(extractSegment(HealthAuthorityNameAndAddress.KEY_QUALIFIER));

    @Getter(lazy = true)
    private final List<SG01> segmentGroup01s = SG01.createMultiple(getEdifactSegments().stream()
        .dropWhile(segment -> !segment.startsWith(SG01.KEY))
        .takeWhile(segment -> !segment.startsWith(SG02.KEY))
        .takeWhile(segment -> !segment.startsWith(MessageTrailer.KEY))
        .collect(toList()));

    @Getter(lazy = true)
    private final SG02 segmentGroup02 = new SG02(getEdifactSegments().stream()
        .dropWhile(segment -> !segment.startsWith(SG02.KEY))
        .skip(1)
        .takeWhile(segment -> !segment.startsWith(MessageTrailer.KEY))
        .collect(toList()));

    @Getter
    @Setter
    private Interchange interchange;

    public Message(final List<String> edifactSegments) {
        super(edifactSegments);
    }

    public String findFirstGpCode() {
        return extractOptionalSegment(GpNameAndAddress.KEY_QUALIFIER)
            .stream()
            .map(GpNameAndAddress::fromString)
            .map(GpNameAndAddress::getIdentifier)
            .findFirst()
            .orElse(DEFAULT_GP_CODE);
    }

    @Override
    public String toString() {
        return String.format("Message{SIS: %s, SMS: %s}",
            getInterchange().getInterchangeHeader().getSequenceNumber(),
            getMessageHeader().getSequenceNumber());
    }
}
