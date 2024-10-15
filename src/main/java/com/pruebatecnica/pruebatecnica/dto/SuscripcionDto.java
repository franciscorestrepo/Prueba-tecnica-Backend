package com.pruebatecnica.pruebatecnica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionDto {
    private String clienteId;
    private Integer fondoId;
    private Integer tipoNotificacion;
    private Double valor;
    private String correo;
    private String celular;
}
