package co.com.pragma.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import co.com.pragma.model.MontoAprobado;
import co.com.pragma.model.Reporte;
import co.com.pragma.model.gateway.DynamoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

class IngresarDatosUseCaseTest {

    private DynamoGateway dynamoGateway;
    private IngresarDatosUseCase useCase;

    @BeforeEach
    void setUp() {
        dynamoGateway = mock(DynamoGateway.class);
        useCase = new IngresarDatosUseCase(dynamoGateway);
    }

    @Test
    void procesarYGuardarReporte_debeConstruirYGuardarReporteCorrectamente() {
        MontoAprobado montoAprobado = mock(MontoAprobado.class);
        when(montoAprobado.getMontoAprobado()).thenReturn("1000");

        Reporte reporteEsperado = Reporte.builder()
                .fecha(LocalDate.now())
                .monto(1000L)
                .build();

        when(dynamoGateway.guardarReporte(any(Reporte.class)))
                .thenReturn(Mono.just(reporteEsperado));

        Mono<Reporte> resultado = useCase.procesarYGuardarReporte(montoAprobado);

        Reporte reporte = resultado.block();
        assertNotNull(reporte);
        assertEquals(1000L, reporte.getMonto());
    }
}