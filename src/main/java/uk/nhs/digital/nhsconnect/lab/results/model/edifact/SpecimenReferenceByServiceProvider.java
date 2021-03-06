package uk.nhs.digital.nhsconnect.lab.results.model.edifact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.message.EdifactValidationException;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.message.Split;

/**
 * Example RFF+STI:CH000064LX'
 */
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@AllArgsConstructor
public class SpecimenReferenceByServiceProvider extends Segment {
    private static final String KEY = "RFF";
    private static final String QUALIFIER = "STI";
    private static final String KEY_QUALIFIER = KEY + PLUS_SEPARATOR + QUALIFIER;

    private final String referenceNumber;

    public static SpecimenReferenceByServiceProvider fromString(String edifactString) {
        if (!edifactString.startsWith(KEY_QUALIFIER)) {
            throw new IllegalArgumentException(
                "Can't create " + SpecimenReferenceByServiceProvider.class.getSimpleName() + " from " + edifactString);
        }

        String[] split = Split.byColon(
            Split.bySegmentTerminator(edifactString)[0]
        );

        return new SpecimenReferenceByServiceProvider(split[1]);
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getValue() {
        return QUALIFIER + COLON_SEPARATOR + referenceNumber;
    }

    @Override
    protected void validateStateful() throws EdifactValidationException {
        // nothing
    }

    @Override
    public void preValidate() throws EdifactValidationException {
        if (referenceNumber.isBlank()) {
            throw new EdifactValidationException(getKey() + ": Specimen Reference number by service provider is blank or missing");
        }
    }
}
