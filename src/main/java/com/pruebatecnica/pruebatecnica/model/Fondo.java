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
@DynamoDBTable(tableName = "fondos")

public class Fondo {
    @DynamoDBHashKey(attributeName = "fondoId")

    private Integer fondoId;

    @DynamoDBAttribute(attributeName = "nombre")
    private String nombre;

    @DynamoDBAttribute(attributeName = "monto_minimo")
    private Double montoMinimo;

    @DynamoDBAttribute(attributeName = "categoria")
    private String categoria;
}
