package com.pruebatecnica.pruebatecnica.controller;

import com.pruebatecnica.pruebatecnica.dto.ApiResponse;
import com.pruebatecnica.pruebatecnica.dto.ClienteInfoDto;
import com.pruebatecnica.pruebatecnica.dto.SuscripcionDto;
import com.pruebatecnica.pruebatecnica.model.Cliente;
import com.pruebatecnica.pruebatecnica.model.Transaccion;
import com.pruebatecnica.pruebatecnica.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://amaris-front.s3-website-us-east-1.amazonaws.com"})
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        Cliente createdCliente = clienteService.save(cliente);
        return ResponseEntity.ok(createdCliente);
    }


    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://amaris-front.s3-website-us-east-1.amazonaws.com"})
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getCliente(@PathVariable("id") String clienteId) {
        Cliente cliente = clienteService.getCliente(clienteId);
        return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
    }


    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://amaris-front.s3-website-us-east-1.amazonaws.com"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable("id") String clienteId) {
        clienteService.deleteCliente(clienteId);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://amaris-front.s3-website-us-east-1.amazonaws.com"})
    @GetMapping
    public ResponseEntity<Iterable<Cliente>> getAllClientes() {
        Iterable<Cliente> clientes = clienteService.getAllClientes();
        return ResponseEntity.ok(clientes);
    }



    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://amaris-front.s3-website-us-east-1.amazonaws.com"})
    @PostMapping("/{id}/transacciones")
    public ResponseEntity<Void> addTransaccionToCliente(@PathVariable("id") String clienteId,
                                                        @RequestBody Transaccion transaccion) {
        clienteService.addTransaccionToCliente(clienteId, transaccion);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://amaris-front.s3-website-us-east-1.amazonaws.com"})
    @GetMapping("/{id}/transacciones")
    public ResponseEntity<List<Transaccion>> getTransaccionesDeCliente(@PathVariable("id") String clienteId) {
        List<Transaccion> transacciones = clienteService.getTransaccionesDeCliente(clienteId);
        return ResponseEntity.ok(transacciones);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://amaris-front.s3-website-us-east-1.amazonaws.com"})
    @PostMapping("/suscribir")
    public ResponseEntity<ApiResponse> subscribeToFondo(@RequestBody SuscripcionDto request) {
        try {
            clienteService.subscribeToFondo(
                    request.getClienteId(),
                    request.getFondoId(),
                    request.getTipoNotificacion(),
                    request.getValor(),
                    request.getCorreo(),
                    request.getCelular()
            );
            ApiResponse response = new ApiResponse(true, "Suscripción exitosa al fondo", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse response = new ApiResponse(false, e.getMessage(), null);
            return ResponseEntity.ok(response);
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://amaris-front.s3-website-us-east-1.amazonaws.com"})
    @PostMapping("/cancelar-suscripcion")
    public ResponseEntity<ApiResponse> cancelarSuscripcion(@RequestBody SuscripcionDto request) {
        try {
            clienteService.cancelarSuscripcion(request.getClienteId(), request.getFondoId());
            ApiResponse response = new ApiResponse(true, "Cancelación exitosa de la suscripción al fondo", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse response = new ApiResponse(false, e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://amaris-front.s3-website-us-east-1.amazonaws.com"})
    @GetMapping("/{id}/info")
    public ResponseEntity<ClienteInfoDto> getClienteInfo(@PathVariable("id") String clienteId) {
        try {
            ClienteInfoDto clienteInfo = clienteService.getClienteInfo(clienteId);
            return ResponseEntity.ok(clienteInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


}