package com.pruebatecnica.pruebatecnica.dto;

import com.pruebatecnica.pruebatecnica.model.Transaccion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {
    private String clienteId;
    private String nombre;
    private List<Transaccion> transacciones;
}
