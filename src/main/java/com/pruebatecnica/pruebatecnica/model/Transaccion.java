package com.pruebatecnica.pruebatecnica.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "transacciones")
public class Transaccion {
    @DynamoDBHashKey(attributeName = "transaccionId")
    private String transaccionId;

    @DynamoDBAttribute(attributeName = "clienteId")
    private String clienteId;

    @DynamoDBAttribute(attributeName = "fondoId")
    private Integer fondoId;

    @DynamoDBAttribute(attributeName = "tipo")
    private String tipo;

    @DynamoDBAttribute(attributeName = "monto")
    private Double monto;

    @DynamoDBAttribute(attributeName = "timestamp")
    private String timestamp;

    @DynamoDBAttribute(attributeName = "estado")
    private String estado;

    @DynamoDBAttribute(attributeName = "mensaje")
    private String mensaje;
}
