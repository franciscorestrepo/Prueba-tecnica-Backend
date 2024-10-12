package com.pruebatecnica.pruebatecnica.service;

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

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,
                          FondoService fondoService,NotificacionService notificacionService) {
        this.clienteRepository = clienteRepository;
        this.fondoService = fondoService;
        this.notificacionService = notificacionService;
    }

    // Método para crear o actualizar un cliente
    public Cliente save(Cliente cliente) {
        if (clienteYaExiste(cliente.getClienteId())) {
            throw new IllegalArgumentException("El cliente con ID " + cliente.getClienteId() + " ya existe.");
        }

        // Asignar saldo inicial si el cliente es nuevo
        if (cliente.getSaldo() == 0) {
            cliente.setSaldo(500000.0); // Saldo inicial
        }

        return clienteRepository.save(cliente);
    }

    // Método para verificar si el cliente ya existe
    private boolean clienteYaExiste(String clienteId) {
        return clienteRepository.existsById(clienteId);
    }

    // Método para obtener un cliente por ID
    public Cliente getCliente(String clienteId) {
        return clienteRepository.findById(clienteId).orElse(null);
    }

    // Método para eliminar un cliente
    public void deleteCliente(String clienteId) {
        clienteRepository.deleteById(clienteId);
    }

    // Método para obtener todos los clientes
    public Iterable<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    // Método para agregar una transacción a un cliente
    public void addTransaccionToCliente(String clienteId, Transaccion transaccion) {
        Cliente cliente = getCliente(clienteId);
        if (cliente != null) {
            if (cliente.getTransacciones() == null) {
                cliente.setTransacciones(new ArrayList<>());
            }
            cliente.getTransacciones().add(transaccion);
            save(cliente);  // Utiliza el método save para actualizar
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    // Método para obtener todas las transacciones de un cliente
    public List<Transaccion> getTransaccionesDeCliente(String clienteId) {
        Cliente cliente = getCliente(clienteId);
        if (cliente != null) {
            return cliente.getTransacciones();
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    // Método para suscribirse a un fondo
    public void subscribeToFondo(String clienteId, Integer fondoId, Integer tipoNotificacion) {
        // Verificar si el cliente existe en la base de datos
        if (!clienteYaExiste(clienteId)) {
            throw new RuntimeException("Cliente no encontrado");
        }

        // Obtener el cliente
        Cliente cliente = getCliente(clienteId);

        // Obtener el fondo
        Fondo fondo = fondoService.getFondo(fondoId);
        if (fondo == null) {
            throw new RuntimeException("Fondo no encontrado");
        }

        // Verificar si el cliente ya está inscrito en el fondo
        if (isClienteInscritoEnFondo(cliente, fondoId)) {
            throw new RuntimeException("El cliente ya está inscrito en el fondo " + fondo.getNombre());
        }

        // Verificar si el saldo es suficiente
        if (cliente.getSaldo() < fondo.getMontoMinimo()) {
            throw new RuntimeException("No tiene saldo disponible para vincularse al fondo " + fondo.getNombre());
        }

        // Crear la transacción
        Transaccion transaccion = crearTransaccion(clienteId, fondoId, fondo.getMontoMinimo(), "Apertura",
                fondo.getNombre());

        // Inicializar la lista de transacciones si es null
        if (cliente.getTransacciones() == null) {
            cliente.setTransacciones(new ArrayList<>());
        }

        // Agregar la transacción al cliente
        cliente.getTransacciones().add(transaccion);

        // Actualizar el saldo del cliente
        cliente.setSaldo(cliente.getSaldo() - fondo.getMontoMinimo());

        // Guardar el cliente actualizado en la base de datos
        clienteRepository.save(cliente);

        // Preparar la notificación
        NotificacionDto notificacion = new NotificacionDto(clienteId, "Suscripción exitosa al fondo "
                + fondo.getNombre(), tipoNotificacion);

        // Enviar la notificación
        notificacionService.enviarNotificacion(notificacion);
    }

    // Método para cancelar la suscripción a un fondo
    public void cancelarSuscripcion(String clienteId, Integer fondoId) {
        // Verificar si el cliente existe
        if (!clienteYaExiste(clienteId)) {
            throw new RuntimeException("Cliente no encontrado");
        }

        // Obtener el cliente
        Cliente cliente = getCliente(clienteId);

        // Verificar si el cliente tiene una transacción de apertura para el fondo
        Transaccion transaccion = cliente.getTransacciones().stream()
                .filter(t -> t.getFondoId().equals(fondoId) && "Apertura".equals(t.getTipo()))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No hay suscripción activa para el fondo " + fondoId));

        // Retornar el monto de vinculación al cliente
        cliente.setSaldo(cliente.getSaldo() + transaccion.getMonto());

        // Cambiar el estado de la transacción a "Cancelación"
        transaccion.setTipo("Cancelación");
        transaccion.setEstado("éxito");
        transaccion.setMensaje("Cancelación exitosa de la suscripción al fondo " + fondoId);

        // Guardar el cliente actualizado en la base de datos
        clienteRepository.save(cliente);
    }

    // Método para verificar si el cliente ya está inscrito en un fondo
    private boolean isClienteInscritoEnFondo(Cliente cliente, Integer fondoId) {
        return cliente.getTransacciones() != null &&
                cliente.getTransacciones().stream().anyMatch(t -> t.getFondoId().equals(fondoId) &&
                        "Apertura".equals(t.getTipo()));
    }

    // Método para crear una transacción
    private Transaccion crearTransaccion(String clienteId, int fondoId, double monto, String tipo, String nombreFondo) {
        Transaccion transaccion = new Transaccion();
        transaccion.setTransaccionId(UUID.randomUUID().toString()); // Generar ID único
        transaccion.setClienteId(clienteId); // Asignar el ID del cliente
        transaccion.setFondoId(fondoId);
        transaccion.setTipo(tipo);
        transaccion.setMonto(monto);
        transaccion.setTimestamp(Instant.now().toString());
        transaccion.setEstado("éxito");
        transaccion.setMensaje("Suscripción exitosa a " + nombreFondo);
        return transaccion;
    }



}
