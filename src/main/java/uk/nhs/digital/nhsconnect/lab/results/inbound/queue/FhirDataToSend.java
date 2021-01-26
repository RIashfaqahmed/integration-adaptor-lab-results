package uk.nhs.digital.nhsconnect.lab.results.inbound.queue;

import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.dstu3.model.Parameters;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.TransactionType;

@Getter
@Setter
public class FhirDataToSend {
    private Object content;
    private String operationId;
    private TransactionType transactionType;

    public FhirDataToSend setContent(final Parameters parameters) {
        this.content = parameters;
        return this;
    }
}