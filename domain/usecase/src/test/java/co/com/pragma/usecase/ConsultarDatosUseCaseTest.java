package co.com.pragma.usecase;

import static org.junit.jupiter.api.Assertions.*;

import co.com.pragma.model.Reporte;
import co.com.pragma.model.ReporteResumen;
import co.com.pragma.model.gateway.DynamoGateway;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import java.util.List;

class ConsultarDatosUseCaseTest {

    @Test
    void listarReportes_debeRetornarReporteResumenCorrecto() {
        DynamoGateway dynamoGateway = Mockito.mock(DynamoGateway.class);
        List<Reporte> reportes = List.of(
            Reporte.builder().monto(100L).build(),
            Reporte.builder().monto(200L).build()
        );
        Mockito.when(dynamoGateway.listarReportes()).thenReturn(Mono.just(reportes));

        ConsultarDatosUseCase useCase = new ConsultarDatosUseCase(dynamoGateway);

        ReporteResumen resumen = useCase.listarReportes().block();

        assertNotNull(resumen);
        assertEquals(300, resumen.getMontoTotal());
        assertEquals(2, resumen.getNumeroDeCreditos());
    }
}