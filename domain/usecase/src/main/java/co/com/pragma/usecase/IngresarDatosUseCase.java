package co.com.pragma.usecase;

import co.com.pragma.model.MontoAprobado;
import co.com.pragma.model.Reporte;
import co.com.pragma.model.gateway.DynamoGateway;
import co.com.pragma.usecase.Utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Log
@AllArgsConstructor
public class IngresarDatosUseCase {

  private final DynamoGateway dynamoGateway;

  public Mono<Reporte> procesarYGuardarReporte(MontoAprobado montoAprobado) {
    log.info(Constants.INICIANDO_PROCESAMIENTO + montoAprobado.getMontoAprobado());
    Reporte reporte = construirReporte(montoAprobado);
    log.info(Constants.REPORTE_CONSTRUIDO + reporte);
    return guardarReporte(reporte);
  }

  private Reporte construirReporte(MontoAprobado montoAprobado) {
    String montoStr = montoAprobado.getMontoAprobado().replace(Constants.DECIMALES, "");
    return Reporte.builder()
            .fecha(LocalDate.now())
            .monto(Long.parseLong(montoStr))
            .build();
  }

  private Mono<Reporte> guardarReporte(Reporte reporte) {
    return dynamoGateway.guardarReporte(reporte)
            .doOnSuccess(r -> log.info(Constants.REPORTE_GUARDADO + r))
            .doOnError(e -> log.severe(Constants.ERROR_GUARDAR_REPORTE + e.getMessage()));
  }
}
