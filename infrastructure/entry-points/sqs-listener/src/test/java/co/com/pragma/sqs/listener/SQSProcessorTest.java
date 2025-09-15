package co.com.pragma.sqs.listener;

import static org.junit.jupiter.api.Assertions.*;


import co.com.pragma.model.MontoAprobado;
import co.com.pragma.usecase.IngresarDatosUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SQSProcessorTest {

    private IngresarDatosUseCase myUseCase;
    private SQSProcessor sqsProcessor;

    @BeforeEach
    void setUp() {
        myUseCase = mock(IngresarDatosUseCase.class);
        sqsProcessor = new SQSProcessor(myUseCase);
    }

    @Test
    void apply_shouldProcessValidMessage() throws JsonProcessingException {
        MontoAprobado monto = new MontoAprobado();
        monto.setMontoAprobado(String.valueOf(1000L));
        String json = new ObjectMapper().writeValueAsString(monto);

        Message message = mock(Message.class);
        when(message.body()).thenReturn(json);

        when(myUseCase.procesarYGuardarReporte(any(MontoAprobado.class))).thenReturn(Mono.empty());

        Mono<Void> result = sqsProcessor.apply(message);
        assertTrue(result.blockOptional().isEmpty());
        verify(myUseCase, times(1)).procesarYGuardarReporte(any(MontoAprobado.class));
    }

    @Test
    void apply_shouldThrowRuntimeExceptionOnInvalidJson() {
        Message message = mock(Message.class);
        when(message.body()).thenReturn("invalid-json");
        assertThrows(RuntimeException.class, () -> sqsProcessor.apply(message));
        verify(myUseCase, never()).procesarYGuardarReporte(any());
    }
}