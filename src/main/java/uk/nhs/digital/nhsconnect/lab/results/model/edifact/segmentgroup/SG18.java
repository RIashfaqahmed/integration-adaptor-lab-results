package uk.nhs.digital.nhsconnect.lab.results.model.edifact.segmentgroup;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.DiagnosticReportCode;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.LaboratoryInvestigation;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.LaboratoryInvestigationResult;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.MessageTrailer;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.ServiceProviderCommentFreeText;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.TestStatus;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SG18 extends SegmentGroup {
    public static final String KEY = "S18";

    // GIS
    @Getter(lazy = true)
    private final DiagnosticReportCode diagnosticReportCode =
        DiagnosticReportCode.fromString(extractSegment(DiagnosticReportCode.KEY));

    // INV+MQ
    @Getter(lazy = true)
    private final LaboratoryInvestigation laboratoryInvestigation =
        LaboratoryInvestigation.fromString(extractSegment(LaboratoryInvestigation.KEY_QUALIFIER));

    // SEQ - Missing?

    // RSL+NV?
    @Getter(lazy = true)
    private final Optional<LaboratoryInvestigationResult> laboratoryInvestigationResult =
        extractOptionalSegment(LaboratoryInvestigationResult.KEY_QUALIFIER)
            .map(LaboratoryInvestigationResult::fromString);

    // STS?
    @Getter(lazy = true)
    private final Optional<TestStatus> testStatus =
        extractOptionalSegment(TestStatus.KEY)
            .map(TestStatus::fromString);

    // FTX{,99}
    @Getter(lazy = true)
    private final List<ServiceProviderCommentFreeText> serviceProviderCommentFreeTexts =
        extractSegments(ServiceProviderCommentFreeText.KEY_QUALIFIER).stream()
            .map(ServiceProviderCommentFreeText::fromString)
            .collect(toList());

    // RFF - missing?

    // S20{,9}
    @Getter(lazy = true)
    private final List<SG20> segmentGroup20s = SG20.createMultiple(getEdifactSegments().stream()
        .dropWhile(segment -> !segment.startsWith(SG20.KEY))
        .takeWhile(segment -> !segment.startsWith(MessageTrailer.KEY))
        .collect(toList()));

    public static List<SG18> createMultiple(@NonNull final List<String> sg18Segments) {
        return splitMultipleSegmentGroups(sg18Segments, KEY).stream()
            .map(SG18::new)
            .collect(toList());
    }

    public SG18(final List<String> edifactSegments) {
        super(edifactSegments);
    }

    @Override
    public String getKey() {
        return KEY;
    }
}
