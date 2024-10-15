package com.pruebatecnica.pruebatecnica.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClienteInfoDto {
    private String clienteId;
    private String nombre;
    private Double saldoTotal;
    private Double saldoInvertido;
    private Double saldoRestante;
}
