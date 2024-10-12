package com.pruebatecnica.pruebatecnica.controller;

import com.pruebatecnica.pruebatecnica.dto.SuscripcionDto;
import com.pruebatecnica.pruebatecnica.model.Cliente;
import com.pruebatecnica.pruebatecnica.model.Transaccion;
import com.pruebatecnica.pruebatecnica.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Endpoint para crear un nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        Cliente createdCliente = clienteService.save(cliente);
        return ResponseEntity.ok(createdCliente);
    }

    // Endpoint para obtener un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getCliente(@PathVariable("id") String clienteId) {
        Cliente cliente = clienteService.getCliente(clienteId);
        return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
    }

    // Endpoint para eliminar un cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable("id") String clienteId) {
        clienteService.deleteCliente(clienteId);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para obtener todos los clientes
    @GetMapping
    public ResponseEntity<Iterable<Cliente>> getAllClientes() {
        Iterable<Cliente> clientes = clienteService.getAllClientes();
        return ResponseEntity.ok(clientes);
    }

    // Endpoint para agregar una transacción a un cliente
    @PostMapping("/{id}/transacciones")
    public ResponseEntity<Void> addTransaccionToCliente(@PathVariable("id") String clienteId,
                                                        @RequestBody Transaccion transaccion) {
        clienteService.addTransaccionToCliente(clienteId, transaccion);
        return ResponseEntity.ok().build();
    }

    // Endpoint para obtener todas las transacciones de un cliente
    @GetMapping("/{id}/transacciones")
    public ResponseEntity<List<Transaccion>> getTransaccionesDeCliente(@PathVariable("id") String clienteId) {
        List<Transaccion> transacciones = clienteService.getTransaccionesDeCliente(clienteId);
        return ResponseEntity.ok(transacciones);
    }

    // Endpoint para suscribirse a un fondo
    @PostMapping("/suscribir")
    public ResponseEntity<String> subscribeToFondo(@RequestBody SuscripcionDto request) {
        try {
          clienteService.subscribeToFondo(request.getClienteId(), request.getFondoId(), request.getTipoNotificacion());
            return ResponseEntity.ok("Suscripción exitosa al fondo");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Endpoint para cancelar la suscripción a un fondo
    @PostMapping("/cancelar-suscripcion")
    public ResponseEntity<String> cancelarSuscripcion(@RequestBody SuscripcionDto request) {
        try {
            clienteService.cancelarSuscripcion(request.getClienteId(), request.getFondoId());
            return ResponseEntity.ok("Cancelación exitosa de la suscripción al fondo");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}