package co.com.pragma.api;

import co.com.pragma.api.utils.Constants;
import co.com.pragma.usecase.ConsultarDatosUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Log
public class Handler {

  private final ConsultarDatosUseCase useCase;

  public Mono<ServerResponse> listarReportes(ServerRequest serverRequest) {
    log.info(Constants.INICIANDO_LISTAR_REPORTES);
    return useCase.listarReportes()
            .doOnNext(reportes -> log.info(Constants.REPORTES_OBTENIDOS + reportes))
            .flatMap(reportes -> ServerResponse.ok().bodyValue(reportes))
            .doOnError(error -> log.info(Constants.ERROR_LISTAR_REPORTES + error.getMessage()));
  }
}
