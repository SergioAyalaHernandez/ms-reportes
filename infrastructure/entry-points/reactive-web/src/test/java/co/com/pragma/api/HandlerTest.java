package co.com.pragma.api;

import co.com.pragma.model.ReporteResumen;
import co.com.pragma.usecase.ConsultarDatosUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HandlerTest {

  @Mock
  private ConsultarDatosUseCase useCase;

  @InjectMocks
  private Handler handler;

  @Test
  void listarReportes_deberiaRetornarServerResponseConReportes() {
    // Arrange
    List<String> reportes = List.of("reporte1", "reporte2");
    when(useCase.listarReportes()).thenReturn(Mono.just(new ReporteResumen()));
    ServerRequest serverRequest = mock(ServerRequest.class);

    // Act
    Mono<ServerResponse> responseMono = handler.listarReportes(serverRequest);

    // Assert
    StepVerifier.create(responseMono)
            .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
            .verifyComplete();
    verify(useCase).listarReportes();
  }
}