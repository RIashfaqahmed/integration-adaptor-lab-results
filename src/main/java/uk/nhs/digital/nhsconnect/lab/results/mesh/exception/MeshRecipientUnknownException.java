package uk.nhs.digital.nhsconnect.lab.results.mesh.exception;

import uk.nhs.digital.nhsconnect.lab.results.rest.exception.BadRequestException;

public class MeshRecipientUnknownException extends BadRequestException {
    public MeshRecipientUnknownException(String message) {
        super(message);
    }
}
