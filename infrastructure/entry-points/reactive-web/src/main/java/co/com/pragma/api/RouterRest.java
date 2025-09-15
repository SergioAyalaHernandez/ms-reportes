package co.com.pragma.api;

import co.com.pragma.model.ReporteResumen;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

  @Bean
  @RouterOperations({
          @RouterOperation(
                  path = "/api/v1/reportes",
                  produces = {"application/json"},
                  method = GET,
                  beanClass = Handler.class,
                  beanMethod = "listarReportes",
                  operation = @Operation(
                          summary = "Listar reportes",
                          description = "Obtiene el monto total y número de créditos",
                          operationId = "listarReportes",
                          responses = {
                                  @ApiResponse(
                                          responseCode = "200",
                                          description = "Reporte obtenido correctamente",
                                          content = @Content(
                                                  mediaType = "application/json",
                                                  schema = @Schema(
                                                          implementation = ReporteResumen.class,
                                                          example = "{\"montoTotal\": 9000000, \"numeroDeCreditos\": 13}"
                                                  )
                                          )
                                  ),
                                  @ApiResponse(
                                          responseCode = "401",
                                          description = "No autorizado"
                                  )
                          }
                  )
          )
  })
  public RouterFunction<ServerResponse> routerFunction(Handler handler) {
    return route(GET("/api/v1/reportes"), handler::listarReportes);
  }
}

