package co.com.pragma.api;

import co.com.pragma.usecase.ConsultarDatosUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;


@WebFluxTest
@ContextConfiguration(classes = {RouterRest.class, Handler.class})
class RouterRestTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private ConsultarDatosUseCase consultarDatosUseCase;


  @Test
  void testListarReportes() {
    Mockito.when(consultarDatosUseCase.listarReportes())
            .thenReturn(null);

    webTestClient.get()
            .uri("/api/v1/reportes")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is5xxServerError()
            .expectBody(String.class)
            .value(userResponse -> {
                      Assertions.assertThat(userResponse)
                              .contains("\"status\":500")
                              .contains("\"error\":\"Internal Server Error\"");
                    }
            );
  }

  @Test
  void testRouterFunctionNullBean() {
    Assertions.assertThatThrownBy(() -> {
      new RouterRest().routerFunction(null);
    }).isInstanceOf(NullPointerException.class);
  }
}
