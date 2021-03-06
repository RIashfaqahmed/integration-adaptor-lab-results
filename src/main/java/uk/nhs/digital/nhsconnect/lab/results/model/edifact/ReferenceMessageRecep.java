package uk.nhs.digital.nhsconnect.lab.results.model.edifact;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.message.EdifactValidationException;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.message.Split;

import java.util.Arrays;

/**
 * Example: RFF+MIS:00000001 CP'
 */
@Getter
@Setter
@RequiredArgsConstructor
public class ReferenceMessageRecep extends Segment {
    public static final String KEY = "RFF";
    public static final String QUALIFIER = "MIS";
    public static final String KEY_QUALIFIER = KEY + "+" + QUALIFIER;

    private final Long messageSequenceNumber;
    private final RecepCode recepCode;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getValue() {
        return String.format("%s:%08d %s", QUALIFIER, messageSequenceNumber, recepCode.getCode());
    }

    @Override
    protected void validateStateful() {
    }

    @Override
    public void preValidate() throws EdifactValidationException {
        if (messageSequenceNumber == null) {
            throw new EdifactValidationException(getKey() + ": Attribute messageSequenceNumber is required");
        }
        if (recepCode == null) {
            throw new EdifactValidationException(getKey() + ": Attribute recepCode is required");
        }
    }

    public static ReferenceMessageRecep fromString(final String edifactString) {
        if (!edifactString.startsWith(KEY_QUALIFIER)) {
            throw new IllegalArgumentException("Can't create " + ReferenceMessageRecep.class.getSimpleName()
                    + " from " + edifactString);
        }
        final String values = Split.byColon(
            Split.byPlus(edifactString)[1]
        )[1];
        final String[] elements = values.split("\\s");
        return new ReferenceMessageRecep(
            Long.parseLong(elements[0]),
            RecepCode.fromCode(elements[1]));
    }

    @Getter
    @RequiredArgsConstructor
    public enum RecepCode {
        SUCCESS("CP", "Translation successful"),
        ERROR("CA", "Translation error"),
        INCOMPLETE("CI", "Translation incomplete due to a fatal error during translation");

        private final String code;
        private final String description;

        public static RecepCode fromCode(final @NonNull String code) {
            return Arrays.stream(RecepCode.values())
                .filter(rc -> code.equals(rc.getCode()))
                .findFirst()
                .orElseThrow();
        }
    }
}
