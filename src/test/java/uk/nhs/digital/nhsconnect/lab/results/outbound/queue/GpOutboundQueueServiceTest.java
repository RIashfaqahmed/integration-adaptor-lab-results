package uk.nhs.digital.nhsconnect.lab.results.outbound.queue;

import org.hl7.fhir.dstu3.model.Parameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import uk.nhs.digital.nhsconnect.lab.results.inbound.queue.FhirDataToSend;
import uk.nhs.digital.nhsconnect.lab.results.utils.CorrelationIdService;
import uk.nhs.digital.nhsconnect.lab.results.utils.JmsHeaders;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GpOutboundQueueServiceTest {

    private static final String CONSERVATION_ID = "ABC123";

    @InjectMocks
    private GpOutboundQueueService gpOutboundQueueService;
    @Mock
    private JmsTemplate jmsTemplate;
    @Mock
    private ObjectSerializer serializer;
    @Mock
    private CorrelationIdService correlationIdService;
    @Mock
    private Session session;
    @Mock
    private TextMessage textMessage;

    @Value("${labresults.amqp.gpOutboundQueueName}")
    private String gpOutboundQueueName;

    @Test
    void publishMessageToGpOutboundQueue() throws JMSException {
        final Parameters parameters = new Parameters();

        final FhirDataToSend fhirDataToSend = new FhirDataToSend()
                .setOperationId("123")
                .setContent(parameters);

        final String serializedData = "some_serialized_data";

        when(serializer.serialize(parameters)).thenReturn(serializedData);
        when(correlationIdService.getCurrentCorrelationId()).thenReturn(CONSERVATION_ID);

        gpOutboundQueueService.publish(fhirDataToSend);

        verify(serializer).serialize(fhirDataToSend.getContent());

        final ArgumentCaptor<MessageCreator> messageCreatorArgumentCaptor = ArgumentCaptor.forClass(MessageCreator.class);

        verify(jmsTemplate).send(eq(gpOutboundQueueName), messageCreatorArgumentCaptor.capture());

        when(session.createTextMessage(serializedData)).thenReturn(textMessage);

        messageCreatorArgumentCaptor.getValue().createMessage(session);

        verify(session).createTextMessage(eq(serializedData));
        verify(textMessage).setStringProperty(JmsHeaders.OPERATION_ID, fhirDataToSend.getOperationId());
        verify(textMessage).setStringProperty(JmsHeaders.CORRELATION_ID, CONSERVATION_ID);

        verify(correlationIdService).getCurrentCorrelationId();
    }

}
