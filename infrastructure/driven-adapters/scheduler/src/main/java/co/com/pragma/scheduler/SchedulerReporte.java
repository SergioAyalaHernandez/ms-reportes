package co.com.pragma.scheduler;

import co.com.pragma.scheduler.utils.Constants;
import co.com.pragma.usecase.ConsultarDatosUseCase;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Log
@AllArgsConstructor
@Component
public class SchedulerReporte {

  private final ConsultarDatosUseCase consultarDatosUseCase;

  @PostConstruct
  public void init() {
    Flux.interval(Duration.ofMinutes(20))
            .doOnNext(tick -> log.info(Constants.LOG_TAREA))
            .flatMap(t -> consultarDatosUseCase.enviarReporteCorreo())
            .subscribe();
  }
}
