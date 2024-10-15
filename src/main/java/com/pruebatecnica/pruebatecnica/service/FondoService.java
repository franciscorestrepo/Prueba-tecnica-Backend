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


    public Fondo save(Fondo fondo) {
        return fondoRepository.save(fondo);
    }


    public Fondo getFondo(Integer fondoId) {
        Optional<Fondo> fondo = fondoRepository.findById(fondoId);
        if (fondo.isPresent()) {
            return fondo.get();
        } else {
            return null;
        }
    }


    public void deleteFondo(Integer fondoId) {
        fondoRepository.deleteById(fondoId);
    }


    public Iterable<Fondo> getAllFondos() {
        return fondoRepository.findAll();
    }


    public boolean puedeVincularse(Cliente cliente, Integer fondoId) {

        Fondo fondo = getFondo(fondoId);
        if (fondo == null) {
            throw new RuntimeException("Fondo no encontrado con ID: " + fondoId);
        }


        if (fondo.getMontoMinimo() <= 0) {
            throw new RuntimeException("El monto mínimo para el fondo " + fondo.getNombre() + " debe ser mayor que cero.");
        }


        if (cliente.getSaldo() >= fondo.getMontoMinimo()) {
            return true;
        } else {
            throw new RuntimeException("No tiene saldo disponible para vincularse al fondo " + fondo.getNombre());
        }
    }

}
