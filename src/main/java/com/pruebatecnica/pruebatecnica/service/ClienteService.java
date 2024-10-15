package com.pruebatecnica.pruebatecnica.service;

import com.pruebatecnica.pruebatecnica.dto.ClienteInfoDto;
import com.pruebatecnica.pruebatecnica.dto.NotificacionDto;
import com.pruebatecnica.pruebatecnica.model.Cliente;
import com.pruebatecnica.pruebatecnica.model.Fondo;
import com.pruebatecnica.pruebatecnica.model.Transaccion;
import com.pruebatecnica.pruebatecnica.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final FondoService fondoService;

    private final NotificacionService notificacionService;

    private static final Double SALDO_INICIAL = 500000.0;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,
                          FondoService fondoService, NotificacionService notificacionService) {
        this.clienteRepository = clienteRepository;
        this.fondoService = fondoService;
        this.notificacionService = notificacionService;
    }


    public ClienteInfoDto getClienteInfo(String clienteId) {
        Cliente cliente = getCliente(clienteId);
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado");
        }


        Double saldoInvertido = calcularSaldoInvertido(cliente.getTransacciones());


        Double saldoRestante =  SALDO_INICIAL - saldoInvertido;


        return new ClienteInfoDto(
                cliente.getClienteId(),
                cliente.getNombre(),
                SALDO_INICIAL,
                saldoInvertido,
                saldoRestante
        );
    }


    public Cliente save(Cliente cliente) {
        if (clienteYaExiste(cliente.getClienteId())) {
            throw new IllegalArgumentException("El cliente con ID " + cliente.getClienteId() + " ya existe.");
        }


        if (cliente.getSaldo() == 0) {
            cliente.setSaldo(500000.0);
        }

        return clienteRepository.save(cliente);
    }


    private boolean clienteYaExiste(String clienteId) {
        return clienteRepository.existsById(clienteId);
    }


    public Cliente getCliente(String clienteId) {
        return clienteRepository.findById(clienteId).orElse(null);
    }


    private Double calcularSaldoInvertido(List<Transaccion> transacciones) {
        if (transacciones == null) {
            return 0.0;
        }

        return transacciones.stream()
                .filter(t -> "Apertura".equals(t.getTipo()))
                .mapToDouble(Transaccion::getMonto)
                .sum();
    }


    public void deleteCliente(String clienteId) {
        clienteRepository.deleteById(clienteId);
    }


    public Iterable<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }



    public void addTransaccionToCliente(String clienteId, Transaccion transaccion) {
        Cliente cliente = getCliente(clienteId);
        if (cliente != null) {
            if (cliente.getTransacciones() == null) {
                cliente.setTransacciones(new ArrayList<>());
            }
            cliente.getTransacciones().add(transaccion);
            save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }


    public List<Transaccion> getTransaccionesDeCliente(String clienteId) {
        Cliente cliente = getCliente(clienteId);
        if (cliente != null) {
            return cliente.getTransacciones();
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }


    public void subscribeToFondo(String clienteId, Integer fondoId, Integer tipoNotificacion, Double valor, String correo, String celular) {

        if (!clienteYaExiste(clienteId)) {
            throw new RuntimeException("Cliente no encontrado");
        }


        Cliente cliente = getCliente(clienteId);


        Fondo fondo = fondoService.getFondo(fondoId);
        if (fondo == null) {
            throw new RuntimeException("Fondo no encontrado");
        }


        if (isClienteInscritoEnFondo(cliente, fondoId)) {
            throw new RuntimeException("El cliente ya está inscrito en el fondo " + fondo.getNombre());
        }


        if (valor == null || valor < fondo.getMontoMinimo() || valor > 500000.0) {
            throw new RuntimeException("El monto debe estar entre " + fondo.getMontoMinimo() + " y 500000.0");
        }


        if (cliente.getSaldo() < valor) {
            throw new RuntimeException("No tiene saldo disponible para vincularse al fondo " + fondo.getNombre() + " con el monto proporcionado.");
        }


        Transaccion transaccion = crearTransaccion(clienteId, fondoId, valor, "Apertura", fondo.getNombre());


        if (cliente.getTransacciones() == null) {
            cliente.setTransacciones(new ArrayList<>());
        }


        cliente.getTransacciones().add(transaccion);


        cliente.setSaldo(cliente.getSaldo() - valor);

        clienteRepository.save(cliente);


        NotificacionDto notificacion = new NotificacionDto(clienteId, "Suscripción exitosa al fondo " + fondo.getNombre(), tipoNotificacion);


        notificacionService.enviarNotificacion(notificacion, correo, celular);
    }


    public void cancelarSuscripcion(String clienteId, Integer fondoId) {

        if (!clienteYaExiste(clienteId)) {
            throw new RuntimeException("Cliente no encontrado");
        }


        Cliente cliente = getCliente(clienteId);


        Fondo fondo = fondoService.getFondo(fondoId);
        if (fondo == null) {
            throw new RuntimeException("Fondo no encontrado con ID: " + fondoId);
        }


        Transaccion transaccion = cliente.getTransacciones().stream()
                .filter(t -> t.getFondoId().equals(fondoId) && "Apertura".equals(t.getTipo()))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No hay suscripción activa para el fondo " + fondoId));


        cliente.setSaldo(cliente.getSaldo() + transaccion.getMonto());



        transaccion.setTipo("Cancelación");
        transaccion.setEstado("éxito");
        transaccion.setMensaje("Cancelación exitosa de la suscripción al fondo " + fondo.getNombre());


        clienteRepository.save(cliente);
    }


    private boolean isClienteInscritoEnFondo(Cliente cliente, Integer fondoId) {
        return cliente.getTransacciones() != null &&
                cliente.getTransacciones().stream().anyMatch(t -> t.getFondoId().equals(fondoId) &&
                        "Apertura".equals(t.getTipo()));
    }


    private Transaccion crearTransaccion(String clienteId, int fondoId, double monto, String tipo, String nombreFondo) {
        Transaccion transaccion = new Transaccion();
        transaccion.setTransaccionId(UUID.randomUUID().toString());
        transaccion.setClienteId(clienteId);
        transaccion.setFondoId(fondoId);
        transaccion.setTipo(tipo);
        transaccion.setMonto(monto);
        transaccion.setTimestamp(Instant.now().toString());
        transaccion.setEstado("éxito");
        transaccion.setMensaje("Suscripción exitosa a " + nombreFondo);
        return transaccion;
    }

}