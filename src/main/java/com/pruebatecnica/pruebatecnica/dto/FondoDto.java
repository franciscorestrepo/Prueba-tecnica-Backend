package com.pruebatecnica.pruebatecnica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FondoDto {

    private Integer fondoId;      // Identificador único del fondo
    private String nombre;       // Nombre del fondo
    private Double montoMinimo;  // Monto mínimo para suscribirse al fondo
    private String categoria;     // Categoría del fondo

}
