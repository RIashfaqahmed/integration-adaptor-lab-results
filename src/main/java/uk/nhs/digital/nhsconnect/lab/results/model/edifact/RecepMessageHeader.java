package uk.nhs.digital.nhsconnect.lab.results.model.edifact;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.message.EdifactValidationException;

@Getter
@Setter
@RequiredArgsConstructor
public class RecepMessageHeader extends Segment {
    private static final String KEY = "UNH";
    private Long sequenceNumber;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getValue() {
        final var formattedSequenceNumber = String.format("%08d", sequenceNumber);
        return formattedSequenceNumber + "+RECEP:0:2:FH";
    }

    @Override
    protected void validateStateful() throws EdifactValidationException {
        if (sequenceNumber == null) {
            throw new EdifactValidationException(getKey() + ": Attribute sequenceNumber is required");
        }
        if (sequenceNumber <= 0) {
            throw new EdifactValidationException(getKey()
                + ": Attribute sequenceNumber must be greater than or equal to 1");
        }
    }

    @Override
    public void preValidate() {
        // Do nothing
    }
}
