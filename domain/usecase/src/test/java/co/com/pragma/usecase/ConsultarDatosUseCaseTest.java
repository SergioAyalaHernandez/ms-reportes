package co.com.pragma.usecase;

import co.com.pragma.model.Reporte;
import co.com.pragma.model.ReporteResumen;
import co.com.pragma.model.ReporteResumenScheduler;
import co.com.pragma.model.gateway.DynamoGateway;
import co.com.pragma.model.gateway.JsonConverter;
import co.com.pragma.model.gateway.ReportSQSGateway;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ConsultarDatosUseCaseTest {

  @Test
  void listarReportes_debeRetornarReporteResumenCorrecto() {
    DynamoGateway dynamoGateway = mock(DynamoGateway.class);
    List<Reporte> reportes = List.of(
            Reporte.builder().monto(100L).build(),
            Reporte.builder().monto(200L).build()
    );
    when(dynamoGateway.listarReportes()).thenReturn(Mono.just(reportes));

    ReportSQSGateway reportSQSGateway = mock(ReportSQSGateway.class);
    JsonConverter jsonConverter = mock(JsonConverter.class);
    ConsultarDatosUseCase useCase = new ConsultarDatosUseCase(dynamoGateway, reportSQSGateway, jsonConverter);
    ReporteResumen resumen = useCase.listarReportes().block();

    assertNotNull(resumen);
    assertEquals(300, resumen.getMontoTotal());
    assertEquals(2, resumen.getNumeroDeCreditos());
  }

  @Test
  void mapearAReporteResumenScheduler_debeMapearCorrectamente() {
    // Arrange
    List<Reporte> reportes = List.of(
            Reporte.builder().monto(100L).build(),
            Reporte.builder().monto(200L).build()
    );
    ConsultarDatosUseCase.DatosReporte datosReporte = new ConsultarDatosUseCase.DatosReporte(300, 2);
    ConsultarDatosUseCase useCase = new ConsultarDatosUseCase(null, null, null);

    // Act
    ReporteResumenScheduler resumenScheduler = useCase.mapearAReporteResumenScheduler(reportes);

    // Assert
    assertNotNull(resumenScheduler);
    assertEquals("sergio.ayala9208@gmail.com", resumenScheduler.getEmail());
    assertEquals(300, resumenScheduler.getMontoTotal());
    assertEquals(2, resumenScheduler.getNumeroDeCreditos());
  }

  @Test
  void calcularMontoTotal_debeCalcularCorrectamente() {
    // Arrange
    List<Reporte> reportes = List.of(
            Reporte.builder().monto(100L).build(),
            Reporte.builder().monto(200L).build()
    );
    ConsultarDatosUseCase useCase = new ConsultarDatosUseCase(null, null, null);

    // Act
    int montoTotal = useCase.calcularMontoTotal(reportes);

    // Assert
    assertEquals(300, montoTotal);
  }

  @Test
  void enviarReporteCorreo_debeEmitirNotificacion() {
    DynamoGateway dynamoGateway = mock(DynamoGateway.class);
    ReportSQSGateway reportSQSGateway = mock(ReportSQSGateway.class);
    JsonConverter jsonConverter = mock(JsonConverter.class);

    List<Reporte> reportes = List.of(
            Reporte.builder().monto(100L).build(),
            Reporte.builder().monto(200L).build()
    );

    when(dynamoGateway.listarReportes()).thenReturn(Mono.just(reportes));
    when(jsonConverter.toJson(any(ReporteResumenScheduler.class)))
            .thenReturn(Optional.of("{\"json\":\"data\"}"));
    when(reportSQSGateway.emit(anyString()))
            .thenReturn(Mono.empty()); // 👈 importante

    ConsultarDatosUseCase useCase = new ConsultarDatosUseCase(dynamoGateway, reportSQSGateway, jsonConverter);

    // Act
    useCase.enviarReporteCorreo().block();

    // Assert
    verify(reportSQSGateway, times(1)).emit("{\"json\":\"data\"}");
  }

}