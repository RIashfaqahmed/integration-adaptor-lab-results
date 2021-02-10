package uk.nhs.digital.nhsconnect.lab.results.translator.mapper;

import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.springframework.stereotype.Component;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.Message;

import java.util.List;

@Component
public class DiagnosticReportMapper {
    public List<DiagnosticReport> map(final Message message) {
        return List.of(new DiagnosticReport());
    }
}
