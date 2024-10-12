package com.pruebatecnica.pruebatecnica.service;

import com.pruebatecnica.pruebatecnica.model.Cliente;
import com.pruebatecnica.pruebatecnica.model.Fondo;
import com.pruebatecnica.pruebatecnica.repository.FondoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class FondoService {
    private final FondoRepository fondoRepository;

    @Autowired
    public FondoService(FondoRepository fondoRepository) {
        this.fondoRepository = fondoRepository;
    }

    // Método para crear o actualizar un fondo
    public Fondo save(Fondo fondo) {
        return fondoRepository.save(fondo);
    }

    // Método para obtener un fondo por ID
    public Fondo getFondo(Integer fondoId) {
        Optional<Fondo> fondo = fondoRepository.findById(fondoId);
        if (fondo.isPresent()) {
            return fondo.get();
        } else {
            return null; // Devuelve null si el fondo no se encuentra
        }
    }

    // Método para eliminar un fondo
    public void deleteFondo(Integer fondoId) {
        fondoRepository.deleteById(fondoId);
    }

    // Método para obtener todos los fondos
    public Iterable<Fondo> getAllFondos() {
        return fondoRepository.findAll();
    }

    // Método para verificar si un cliente puede vincularse a un fondo
    public boolean puedeVincularse(Cliente cliente, Integer fondoId) {
        // Verifica si el fondo existe en la base de datos
        Fondo fondo = getFondo(fondoId);
        if (fondo == null) {
            throw new RuntimeException("Fondo no encontrado con ID: " + fondoId);
        }

        // Verifica que el monto mínimo del fondo sea mayor que cero
        if (fondo.getMontoMinimo() <= 0) {
            throw new RuntimeException("El monto mínimo para el fondo " + fondo.getNombre() + " debe ser mayor que cero.");
        }

        // Compara el saldo del cliente con el monto mínimo del fondo
        if (cliente.getSaldo() >= fondo.getMontoMinimo()) {
            return true; // Puede vincularse
        } else {
            throw new RuntimeException("No tiene saldo disponible para vincularse al fondo " + fondo.getNombre());
        }
    }

}
