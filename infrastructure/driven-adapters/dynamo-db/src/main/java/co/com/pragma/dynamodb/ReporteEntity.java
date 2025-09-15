package co.com.pragma.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class ReporteEntity {

    private String id;
    private String fecha;
    private Long monto;

    public ReporteEntity() {}

    public ReporteEntity(String id, String fecha, Long monto) {
        this.id = id;
        this.fecha = fecha;
        this.monto = monto;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbAttribute("fecha")
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @DynamoDbAttribute("monto")
    public Long getMonto() {
        return monto;
    }

    public void setMonto(Long monto) {
        this.monto = monto;
    }
}
