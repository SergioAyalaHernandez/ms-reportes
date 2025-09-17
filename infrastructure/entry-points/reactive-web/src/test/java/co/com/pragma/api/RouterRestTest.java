package co.com.pragma.api;

import co.com.pragma.model.ReporteResumen;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouterRestTest {

  @Mock
  private Handler handler;

  @Test
  void listarReportes_retornaReporteResumen() {
    // Arrange
    ReporteResumen resumen = ReporteResumen.builder()
            .montoTotal(9000000)
            .numeroDeCreditos(13)
            .build();

    Mono<ServerResponse> validResponse = Mono.just(
            ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(resumen).block()
    );

    when(handler.listarReportes(any(ServerRequest.class)))
            .thenReturn(validResponse);

    RouterRest routerRest = new RouterRest();

    WebTestClient client = WebTestClient
            .bindToRouterFunction(routerRest.routerFunction(handler))
            .build();

    // Act & Assert
    client.get()
            .uri("/api/v1/reportes")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.montoTotal").isEqualTo(9000000)
            .jsonPath("$.numeroDeCreditos").isEqualTo(13);

    verify(handler, times(1)).listarReportes(any(ServerRequest.class));
  }

  @Test
  void listarReportes_verificaRuta() {
    // Arrange - Mock con respuesta simple
    Mono<ServerResponse> simpleResponse = Mono.just(
            ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"status\":\"ok\"}").block()
    );

    when(handler.listarReportes(any(ServerRequest.class)))
            .thenReturn(simpleResponse);

    RouterRest routerRest = new RouterRest();

    WebTestClient client = WebTestClient
            .bindToRouterFunction(routerRest.routerFunction(handler))
            .build();

    client.get()
            .uri("/api/v1/reportes")
            .exchange()
            .expectStatus().isOk();

    client.get()
            .uri("/api/v1/reportes/otro")
            .exchange()
            .expectStatus().isNotFound();

    verify(handler, times(1)).listarReportes(any(ServerRequest.class));
  }

  @Test
  void listarReportes_soloGET_otrasPeticiones404() {
    // Arrange
    RouterRest routerRest = new RouterRest();

    WebTestClient client = WebTestClient
            .bindToRouterFunction(routerRest.routerFunction(handler))
            .build();

    client.post()
            .uri("/api/v1/reportes")
            .exchange()
            .expectStatus().isNotFound();

    client.put()
            .uri("/api/v1/reportes")
            .exchange()
            .expectStatus().isNotFound();

    client.delete()
            .uri("/api/v1/reportes")
            .exchange()
            .expectStatus().isNotFound();

    verify(handler, never()).listarReportes(any());
  }

  @Test
  void routerFunction_noEsNull() {
    // Arrange & Act
    RouterRest routerRest = new RouterRest();
    var routerFunction = routerRest.routerFunction(handler);

    // Assert
    assert routerFunction != null;
  }
}
