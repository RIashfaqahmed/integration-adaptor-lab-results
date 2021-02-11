package uk.nhs.digital.nhsconnect.lab.results.utils;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CorrelationIdService {

    private static final String MDC_KEY = "CorrelationId";

    public void applyCorrelationId(String id) {
        MDC.put(MDC_KEY, id);
    }

    public void applyRandomCorrelationId() {
        var id = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        applyCorrelationId(id);
    }

    public void resetCorrelationId() {
        MDC.remove(MDC_KEY);
    }

    public String getCurrentCorrelationId() {
        return MDC.get(MDC_KEY);
    }

}
