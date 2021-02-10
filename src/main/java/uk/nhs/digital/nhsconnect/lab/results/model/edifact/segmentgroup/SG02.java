package uk.nhs.digital.nhsconnect.lab.results.model.edifact.segmentgroup;

import static java.util.stream.Collectors.toList;

import java.util.List;
import lombok.Getter;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.DiagnosticReportCode;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.DiagnosticReportDateIssued;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.DiagnosticReportStatus;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.MessageTrailer;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.ReferenceDiagnosticReport;

public class SG02 extends SegmentGroup {
    public static final String KEY = "S02";

    // GIS
    @Getter(lazy = true)
    private final DiagnosticReportCode diagnosticReportCode =
        DiagnosticReportCode.fromString(extractSegment(DiagnosticReportCode.KEY));

    // RFF+SRI
    @Getter(lazy = true)
    private final ReferenceDiagnosticReport referenceDiagnosticReport =
        ReferenceDiagnosticReport.fromString(extractSegment(ReferenceDiagnosticReport.KEY_QUALIFIER));

    // STS
    @Getter(lazy = true)
    private final DiagnosticReportStatus diagnosticReportStatus =
        DiagnosticReportStatus.fromString(extractSegment(DiagnosticReportStatus.KEY));

    // DTM+ISR
    @Getter(lazy = true)
    private final DiagnosticReportDateIssued diagnosticReportDateIssued =
        DiagnosticReportDateIssued.fromString(extractSegment(DiagnosticReportDateIssued.KEY_QUALIFIER));

    // S06
    @Getter(lazy = true)
    private final SG06 segmentGroup06 = new SG06(getEdifactSegments().stream()
        .dropWhile(segment -> !segment.startsWith(SG06.KEY))
        .skip(1)
        .takeWhile(segment -> !segment.startsWith(MessageTrailer.KEY))
        .collect(toList()));

    public SG02(final List<String> edifactSegments) {
        super(edifactSegments);
    }

    @Override
    public String getKey() {
        return KEY;
    }
}
