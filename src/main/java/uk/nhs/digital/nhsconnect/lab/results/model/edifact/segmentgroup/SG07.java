package uk.nhs.digital.nhsconnect.lab.results.model.edifact.segmentgroup;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.PersonDateOfBirth;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.PersonName;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.PersonSex;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SG07 extends SegmentGroup {
    public static final String KEY = "S07";

    // PNA+PAT
    @Getter(lazy = true)
    private final PersonName personName = PersonName.fromString(extractSegment(PersonName.KEY_QUALIFIER));

    // DTM+329?
    @Getter(lazy = true)
    private final Optional<PersonDateOfBirth> personDateOfBirth =
        extractOptionalSegment(PersonDateOfBirth.KEY_QUALIFIER)
            .map(PersonDateOfBirth::fromString);

    // PDI?
    @Getter(lazy = true)
    private final Optional<PersonSex> personSex =
        extractOptionalSegment(PersonSex.KEY)
            .map(PersonSex::fromString);

    public SG07(final List<String> edifactSegments) {
        super(edifactSegments);
    }

    @Override
    public String getKey() {
        return KEY;
    }
}
