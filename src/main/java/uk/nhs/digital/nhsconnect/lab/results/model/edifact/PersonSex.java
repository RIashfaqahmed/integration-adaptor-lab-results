package uk.nhs.digital.nhsconnect.lab.results.model.edifact;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.message.EdifactValidationException;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.message.Split;

/**
 * Example PDI+2'
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@Builder
public class PersonSex extends Segment {
    protected static final String KEY = "PDI";

    @NonNull
    private final Gender gender;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getValue() {
        return gender.getCode();
    }

    @Override
    protected void validateStateful() throws EdifactValidationException {
    }

    @Override
    public void preValidate() throws EdifactValidationException {
    }

    public static PersonSex fromString(final String edifactString) {
        if (!edifactString.startsWith(KEY)) {
            throw new IllegalArgumentException("Can't create " + PersonSex.class.getSimpleName() + " from " + edifactString);
        }
        final String[] components = Split.byPlus(Split.bySegmentTerminator(edifactString)[0]);
        final PersonSexBuilder builder = PersonSex.builder();
        if (components.length > 0) {
            final Gender gender = Gender.fromCode(components[1]);
            builder.gender(gender);
        }
        return builder.build();
    }

}
