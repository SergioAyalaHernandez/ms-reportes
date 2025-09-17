package co.com.pragma.dynamodb.helper;

import co.com.pragma.model.gateway.JsonConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class JacksonJsonConverter implements JsonConverter {

  private final ObjectMapper objectMapper;

  @Override
  public Optional<String> toJson(Object object) {
    try {
      return Optional.of(objectMapper.writeValueAsString(object));
    } catch (JsonProcessingException e) {
      return Optional.empty();
    }
  }
}
