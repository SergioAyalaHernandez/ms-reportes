package co.com.pragma.dynamodb.helper;

import co.com.pragma.dynamodb.ReporteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class TestAdapter extends TemplateAdapterOperations<String, String, ReporteEntity> {
  public TestAdapter(DynamoDbEnhancedAsyncClient client, ObjectMapper mapper) {
    super(client, mapper, r -> r.getId(), "table_name");
  }
}

class TemplateAdapterOperationsTest {

  @Mock
  private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

  @Mock
  private ObjectMapper mapper;


  @Mock
  private DynamoDbAsyncTable<ReporteEntity> customerTable;

  private ReporteEntity reporteEntity;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    when(dynamoDbEnhancedAsyncClient.table("table_name", TableSchema.fromBean(ReporteEntity.class)))
            .thenReturn(customerTable);

    reporteEntity = new ReporteEntity();
    reporteEntity.setId("id");
    reporteEntity.setFecha("atr1");
    reporteEntity.setMonto(1000L);
  }

  @Test
  void testGetTableReturnsTable() {
    TestAdapter adapter = new TestAdapter(dynamoDbEnhancedAsyncClient, mapper);
    assertNotNull(adapter.getTable());
  }

  @Test
  void testToModelReturnsEntity() {
    TestAdapter adapter = new TestAdapter(dynamoDbEnhancedAsyncClient, mapper);
    String result = adapter.toModel(reporteEntity);
    assertNotNull(result);
  }

  @Test
  void testToModelReturnsNullWhenDataIsNull() {
    TestAdapter adapter = new TestAdapter(dynamoDbEnhancedAsyncClient, mapper);
    String result = adapter.toModel(null);
    assertNull(result);
  }
}