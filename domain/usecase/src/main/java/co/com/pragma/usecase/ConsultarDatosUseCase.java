package co.com.pragma.usecase;

import co.com.pragma.model.Reporte;
import co.com.pragma.model.ReporteResumen;
import co.com.pragma.model.gateway.DynamoGateway;
import co.com.pragma.usecase.Utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.List;

@Log
@AllArgsConstructor
public class ConsultarDatosUseCase {

  private final DynamoGateway dynamoGateway;

  public Mono<ReporteResumen> listarReportes() {
    log.info(Constants.INICIANDO_CONSULTA);
    return dynamoGateway.listarReportes()
            .map(this::mapearAReporteResumen);
  }

  private ReporteResumen mapearAReporteResumen(List<Reporte> reportes) {
    log.info(Constants.CANTIDAD_REPORTES + reportes.size());
    int montoTotal = calcularMontoTotal(reportes);
    log.info(Constants.MONTO_TOTAL + montoTotal);
    return ReporteResumen.builder()
            .montoTotal(montoTotal)
            .numeroDeCreditos(reportes.size())
            .build();
  }

  private int calcularMontoTotal(List<Reporte> reportes) {
    return Math.toIntExact(reportes.stream()
            .mapToLong(Reporte::getMonto)
            .sum());
  }

}
