package co.com.pragma.usecase;

import co.com.pragma.model.Reporte;
import co.com.pragma.model.ReporteResumen;
import co.com.pragma.model.ReporteResumenScheduler;
import co.com.pragma.model.gateway.DynamoGateway;
import co.com.pragma.model.gateway.JsonConverter;
import co.com.pragma.model.gateway.ReportSQSGateway;
import co.com.pragma.usecase.Utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Log
@AllArgsConstructor
public class ConsultarDatosUseCase {

  private final DynamoGateway dynamoGateway;

  private final ReportSQSGateway reportSQSGateway;

  private final JsonConverter jsonConverter;

  public Mono<ReporteResumen> listarReportes() {
    log.info(Constants.INICIANDO_CONSULTA);
    return dynamoGateway.listarReportes()
            .map(this::mapearAReporteResumen);
  }

  private DatosReporte obtenerDatosReporte(List<Reporte> reportes) {
    log.info(Constants.CANTIDAD_REPORTES + reportes.size());
    int montoTotal = calcularMontoTotal(reportes);
    log.info(Constants.MONTO_TOTAL + montoTotal);
    return new DatosReporte(montoTotal, reportes.size());
  }

  private ReporteResumen mapearAReporteResumen(List<Reporte> reportes) {
    DatosReporte datos = obtenerDatosReporte(reportes);
    return ReporteResumen.builder()
            .montoTotal(datos.montoTotal())
            .numeroDeCreditos(datos.numeroDeCreditos())
            .build();
  }

  ReporteResumenScheduler mapearAReporteResumenScheduler(List<Reporte> reportes) {
    DatosReporte datos = obtenerDatosReporte(reportes);
    return ReporteResumenScheduler.builder()
            .email(Constants.CORREO_ADMIN)
            .montoTotal(datos.montoTotal())
            .numeroDeCreditos(datos.numeroDeCreditos())
            .build();
  }

  record DatosReporte(int montoTotal, int numeroDeCreditos) {}

  int calcularMontoTotal(List<Reporte> reportes) {
    return Math.toIntExact(reportes.stream()
            .mapToLong(Reporte::getMonto)
            .sum());
  }

  public Mono<Void> enviarReporteCorreo() {
    log.info(Constants.INICIANDO_CONSULTA_SCHEDULER);
    return dynamoGateway.listarReportes()
            .map(this::mapearAReporteResumenScheduler)
            .doOnNext(this::emitNotification)
            .then();
  }

  private void emitNotification(ReporteResumenScheduler reporte) {
    convertToJson(reporte).ifPresent(reporteJson ->
            reportSQSGateway.emit(reporteJson).subscribe()
    );
  }

  private Optional<String> convertToJson(ReporteResumenScheduler reporteResumenScheduler) {
    return jsonConverter.toJson(reporteResumenScheduler);
  }

}
