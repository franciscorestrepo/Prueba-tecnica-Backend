package com.pruebatecnica.pruebatecnica.controller;


import com.pruebatecnica.pruebatecnica.model.Fondo;
import com.pruebatecnica.pruebatecnica.service.FondoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fondos")
public class FondoController {
    private final FondoService fondoService;

    @Autowired
    public FondoController(FondoService fondoService) {
        this.fondoService = fondoService;
    }

    // Crear o actualizar un fondo
    @PostMapping
    public ResponseEntity<Fondo> createFondo(@RequestBody Fondo fondo) {
        Fondo createdFondo = fondoService.save(fondo);
        return ResponseEntity.ok(createdFondo);
    }

    // Obtener un fondo por ID
    @GetMapping("/{fondoId}")
    public ResponseEntity<Fondo> getFondo(@PathVariable Integer fondoId) {
        Fondo fondo = fondoService.getFondo(fondoId);
        return ResponseEntity.ok(fondo);
    }

    // Obtener todos los fondos
    @GetMapping
    public ResponseEntity<List<Fondo>> getAllFondos() {
        Iterable<Fondo> fondos = fondoService.getAllFondos();
        return ResponseEntity.ok((List<Fondo>) fondos);
    }

    // Actualizar un fondo
    @PutMapping("/{fondoId}")
    public ResponseEntity<Fondo> updateFondo(@PathVariable Integer fondoId, @RequestBody Fondo fondo) {
        fondo.setFondoId(fondoId); // Asegurarse de que el ID se establece correctamente
        Fondo updatedFondo = fondoService.save(fondo);
        return ResponseEntity.ok(updatedFondo);
    }

    // Eliminar un fondo
    @DeleteMapping("/{fondoId}")
    public ResponseEntity<Void> deleteFondo(@PathVariable Integer fondoId) {
        fondoService.deleteFondo(fondoId);
        return ResponseEntity.noContent().build();
    }

}
