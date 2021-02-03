package uk.nhs.digital.nhsconnect.lab.results.model.edifact;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.message.EdifactValidationException;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.message.Split;

/**
 * Example: NAD+PO+G3380314:900++SCOTT'
 */
@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class RequesterNameAndAddress extends Segment {

    private static final String KEY = "NAD";
    private static final String QUALIFIER = "PO";
    private static final String KEY_QUALIFIER = KEY + "+" + QUALIFIER;
    private static final int REQUESTER_NAME_INDEX_IN_EDIFACT_STRING = 4;

    @NonNull
    private final String identifier;
    @NonNull
    private final HealthcareRegistrationIdentificationCode healthcareRegistrationIdentificationCode;
    @NonNull
    private final String requesterName;

    public static RequesterNameAndAddress fromString(String edifactString) {
        if (!edifactString.startsWith(RequesterNameAndAddress.KEY_QUALIFIER)) {
            throw new IllegalArgumentException("Can't create " + RequesterNameAndAddress.class.getSimpleName() + " from " + edifactString);
        }

        String[] keySplit = Split.byPlus(edifactString);
        String identifier = Split.byColon(keySplit[2])[0];
        String code = Split.byColon(keySplit[2])[1];
        String requesterName = keySplit[REQUESTER_NAME_INDEX_IN_EDIFACT_STRING];

        return new RequesterNameAndAddress(identifier, HealthcareRegistrationIdentificationCode.fromCode(code), requesterName);
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getValue() {
        return QUALIFIER + "+" + identifier + ":" + healthcareRegistrationIdentificationCode.getCode() + "++" + requesterName;
    }

    @Override
    protected void validateStateful() throws EdifactValidationException {

    }

    @Override
    public void preValidate() throws EdifactValidationException {
        if (identifier.isBlank()) {
            throw new EdifactValidationException(getKey() + ": Attribute identifier is required");
        }

        if (healthcareRegistrationIdentificationCode.getCode().isBlank()) {
            throw new EdifactValidationException(getKey() + ": Attribute code in healthcareRegistrationIdentificationCode is required");
        }

        if (requesterName.isBlank()) {
            throw new EdifactValidationException(getKey() + ": Attribute requesterName is required");
        }
    }
}