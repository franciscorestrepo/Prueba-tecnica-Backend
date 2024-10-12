package com.pruebatecnica.pruebatecnica.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDto {

    private String clienteId;
    private String mensaje;
    private Integer tipo;

}
