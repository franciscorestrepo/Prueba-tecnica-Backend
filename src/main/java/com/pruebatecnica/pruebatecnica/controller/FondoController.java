package com.pruebatecnica.pruebatecnica.controller;


import com.pruebatecnica.pruebatecnica.model.Fondo;
import com.pruebatecnica.pruebatecnica.service.FondoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://amaris-front.s3-website-us-east-1.amazonaws.com"})
@RequestMapping("/fondos")
public class FondoController {
    private final FondoService fondoService;

    @Autowired
    public FondoController(FondoService fondoService) {
        this.fondoService = fondoService;
    }


    @PostMapping
    public ResponseEntity<Fondo> createFondo(@RequestBody Fondo fondo) {
        Fondo createdFondo = fondoService.save(fondo);
        return ResponseEntity.ok(createdFondo);
    }


    @GetMapping("/{fondoId}")
    public ResponseEntity<Fondo> getFondo(@PathVariable Integer fondoId) {
        Fondo fondo = fondoService.getFondo(fondoId);
        return ResponseEntity.ok(fondo);
    }


    @GetMapping
    public ResponseEntity<List<Fondo>> getAllFondos() {
        Iterable<Fondo> fondos = fondoService.getAllFondos();
        return ResponseEntity.ok((List<Fondo>) fondos);
    }


    @PutMapping("/{fondoId}")
    public ResponseEntity<Fondo> updateFondo(@PathVariable Integer fondoId, @RequestBody Fondo fondo) {
        fondo.setFondoId(fondoId);
        Fondo updatedFondo = fondoService.save(fondo);
        return ResponseEntity.ok(updatedFondo);
    }


    @DeleteMapping("/{fondoId}")
    public ResponseEntity<Void> deleteFondo(@PathVariable Integer fondoId) {
        fondoService.deleteFondo(fondoId);
        return ResponseEntity.noContent().build();
    }

}
