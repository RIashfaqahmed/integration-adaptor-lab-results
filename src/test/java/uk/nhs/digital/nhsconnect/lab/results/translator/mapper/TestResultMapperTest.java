package uk.nhs.digital.nhsconnect.lab.results.translator.mapper;

import org.junit.jupiter.api.Test;
import uk.nhs.digital.nhsconnect.lab.results.model.edifact.Message;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestResultMapperTest {

    @Test
    void testMapMessageToObservation() {
        final Message message = new Message(new ArrayList<>());
        assertTrue(new TestResultMapper().map(message).isEmpty());
    }
}
