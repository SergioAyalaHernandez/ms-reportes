package co.com.pragma.dynamodb;

import co.com.pragma.model.Reporte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class DynamoDBTemplateAdapterTest {

    private DynamoDBTemplateAdapter adapter;
    private DynamoDbEnhancedAsyncClient client;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        client = Mockito.mock(DynamoDbEnhancedAsyncClient.class);
        mapper = Mockito.mock(ObjectMapper.class);
        adapter = Mockito.spy(new DynamoDBTemplateAdapter(client, mapper));

        DynamoDbAsyncTable<ReporteEntity> tableMock = Mockito.mock(DynamoDbAsyncTable.class);
        Mockito.doReturn(tableMock).when(adapter).getTable();

        // Mock para scan/query -> PagePublisher con página vacía
        PagePublisher<ReporteEntity> pagePublisher = subscriber -> {
            Page<ReporteEntity> page = Mockito.mock(Page.class);
            Mockito.when(page.items()).thenReturn(List.of()); // lista vacía
            subscriber.onNext(page);
            subscriber.onComplete();
        };

        Mockito.when(tableMock.scan((ScanEnhancedRequest) Mockito.any())).thenReturn(pagePublisher);
        Mockito.when(tableMock.query((QueryEnhancedRequest) Mockito.any())).thenReturn(pagePublisher);

        // Mock correcto para putItem(T item)
        Mockito.when(tableMock.putItem(Mockito.any(ReporteEntity.class)))
                .thenReturn(CompletableFuture.completedFuture(null));
    }



    @Test
    void testListarPorMonto() {
        Mono<List<Reporte>> result = adapter.listarPorMonto(100L);
        assertNotNull(result);
        result.subscribe(list -> assertTrue(list.isEmpty()));
    }

    @Test
    void testListarReportes() {
        Mono<List<Reporte>> result = adapter.listarReportes();
        assertNotNull(result);
    }
    @Test
    void testGuardarReporte() {
        Reporte reporte = new Reporte("id", null, 200L);
        Mono<Reporte> result = adapter.guardarReporte(reporte);
        assertNotNull(result);
        result.subscribe(r -> {
            assertEquals("id", r.getId());
            assertEquals(200L, r.getMonto());
        });
    }
}