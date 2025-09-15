package co.com.pragma.model.gateway;

import co.com.pragma.model.Reporte;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DynamoGateway {
    Mono<List<Reporte>> listarReportes();
    Mono<Reporte> guardarReporte(Reporte reporte);
}
