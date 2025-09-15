package co.com.pragma.sqs.listener;

import co.com.pragma.model.MontoAprobado;
import co.com.pragma.sqs.listener.exceptions.MiExcepcionPersonalizada;
import co.com.pragma.sqs.listener.utils.Constats;
import co.com.pragma.usecase.IngresarDatosUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
  private final IngresarDatosUseCase myUseCase;

  @Override
  public Mono<Void> apply(Message message) {
    MontoAprobado montoAprobado;
    try {
      montoAprobado = new ObjectMapper()
              .readValue(message.body(), MontoAprobado.class);
    } catch (JsonProcessingException e) {
      throw new MiExcepcionPersonalizada(Constats.ERROR_GUARDAR_REPORTE + e.getMessage());
    }
    log.info(Constats.LOG_BODY_MESSAGE + "{}", montoAprobado.getMontoAprobado());
    myUseCase.procesarYGuardarReporte(montoAprobado).subscribe();
    return Mono.empty();
  }
}
