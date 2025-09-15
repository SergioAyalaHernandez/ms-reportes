package co.com.pragma.dynamodb;

import co.com.pragma.dynamodb.helper.TemplateAdapterOperations;
import co.com.pragma.model.Reporte;
import co.com.pragma.model.gateway.DynamoGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<Reporte, String, ReporteEntity> implements DynamoGateway {

  public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
    super(
            connectionFactory,
            mapper,
            d -> mapper.map((Object) d, Reporte.class),
            "reporte"
    );
  }

  public Mono<List<Reporte>> listarPorMonto(Long montoMinimo) {
    Map<String, AttributeValue> values = new HashMap<>();
    values.put(":monto", AttributeValue.builder().n(montoMinimo.toString()).build());

    Expression expression = Expression.builder()
            .expression("monto >= :monto")
            .expressionValues(values)
            .build();

    ScanEnhancedRequest request = ScanEnhancedRequest.builder()
            .filterExpression(expression)
            .build();

    return Mono.from(getTable().scan(request))
            .map(page -> page.items().stream().map(this::toModel).toList());
  }


  @Override
  @SuppressWarnings("all")
  public Mono<List<Reporte>> listarReportes() {
    return listarPorMonto(0L);
  }

  @Override
  public Mono<Reporte> guardarReporte(Reporte reporte) {
    String id =  UUID.randomUUID().toString();
    ReporteEntity entity = new ReporteEntity(
            id,
            reporte.getFecha() != null ? reporte.getFecha().toString() : null,
            reporte.getMonto()
    );

    return Mono.fromFuture(getTable().putItem(entity))
            .thenReturn(new Reporte(id, reporte.getFecha(), reporte.getMonto()));
  }

}
