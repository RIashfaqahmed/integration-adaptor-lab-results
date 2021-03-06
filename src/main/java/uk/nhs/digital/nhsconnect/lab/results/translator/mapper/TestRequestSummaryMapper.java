package uk.nhs.digital.nhsconnect.lab.results.translator.mapper;

import org.hl7.fhir.dstu3.model.ProcedureRequest;
import org.springframework.stereotype.Component;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.Message;

import java.util.Optional;

@Component
public class TestRequestSummaryMapper {
    public Optional<ProcedureRequest> map(final Message message) {
        return Optional.empty();
    }
}
