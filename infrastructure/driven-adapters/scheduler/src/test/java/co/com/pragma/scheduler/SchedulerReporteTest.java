package co.com.pragma.scheduler;

import co.com.pragma.usecase.ConsultarDatosUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

import static org.mockito.Mockito.*;

class SchedulerReporteTest {

  private ConsultarDatosUseCase consultarDatosUseCase;
  private SchedulerReporte schedulerReporte;

  @BeforeEach
  void setUp() {
    consultarDatosUseCase = Mockito.mock(ConsultarDatosUseCase.class);
    schedulerReporte = new SchedulerReporte(consultarDatosUseCase);
  }

  @Test
  void deberiaEjecutarTareaCadaMinuto() {
    // Arrange
    when(consultarDatosUseCase.enviarReporteCorreo()).thenReturn(Mono.empty());
    VirtualTimeScheduler.getOrSet();

    // Act
    schedulerReporte.init();
    VirtualTimeScheduler.get().advanceTimeBy(Duration.ofMinutes(3));

    // Assert
    verify(consultarDatosUseCase, times(3)).enviarReporteCorreo();
  }
}
