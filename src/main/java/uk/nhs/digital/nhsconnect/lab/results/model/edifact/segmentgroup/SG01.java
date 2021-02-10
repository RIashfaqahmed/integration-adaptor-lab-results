package uk.nhs.digital.nhsconnect.lab.results.model.edifact.segmentgroup;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.MessageTrailer;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.PerformingOrganisationNameAndAddress;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.RequesterNameAndAddress;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.ServiceProvider;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SG01 extends SegmentGroup {
    public static final String KEY = "S01";

    // NAD+SLA?
    @Getter(lazy = true)
    private final Optional<PerformingOrganisationNameAndAddress> performingOrganisationNameAndAddress =
        extractOptionalSegment(PerformingOrganisationNameAndAddress.KEY_QUALIFIER)
            .map(PerformingOrganisationNameAndAddress::fromString);

    // NAD+PO?
    @Getter(lazy = true)
    private final Optional<RequesterNameAndAddress> requesterNameAndAddress =
        extractOptionalSegment(RequesterNameAndAddress.KEY_QUALIFIER)
            .map(RequesterNameAndAddress::fromString);

    // SPR
    @Getter(lazy = true)
    private final ServiceProvider serviceProvider = ServiceProvider.fromString(extractSegment(ServiceProvider.KEY));

    public static List<SG01> createMultiple(@NonNull final List<String> sg01Segments) {
        return splitMultipleSegmentGroups(sg01Segments, KEY).stream()
            .map(SG01::new)
            .collect(toList());
    }

    public SG01(final List<String> edifactSegments) {
        super(edifactSegments);
    }

    @Override
    public String getKey() {
        return KEY;
    }

    public static Optional<SG01> read(final ListIterator<String> it) {
        if (!it.hasNext()) {
            return Optional.empty();
        }
        if (!it.next().startsWith(KEY)) {
            it.previous();
            return Optional.empty();
        }

        final var forSG01 = new ArrayList<String>();
        while (it.hasNext()) {
            final var next = it.next();
            if (next.startsWith(KEY) || next.startsWith(SG02.KEY) || next.startsWith(MessageTrailer.KEY)) {
                break;
            }

            forSG01.add(next);
        }

        if (it.hasNext()) {
            // if we stopped reading an SG01 because we ran out of lines to read, then we shouldn't step back
            it.previous();
        }
        return Optional.of(new SG01(forSG01));
    }
}
