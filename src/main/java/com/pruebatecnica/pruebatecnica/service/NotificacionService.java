package com.pruebatecnica.pruebatecnica.service;

import com.pruebatecnica.pruebatecnica.dto.NotificacionDto;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    public void enviarNotificacion(NotificacionDto notificacion) {
        switch (notificacion.getTipo()) {
            case 1: // EMAIL
                enviarEmail(notificacion.getClienteId(), notificacion.getMensaje());
                break;
            case 2: // SMS
                enviarSMS(notificacion.getClienteId(), notificacion.getMensaje());
                break;
            default:
                throw new IllegalArgumentException("Tipo de notificación no válido: " + notificacion.getTipo());
        }
    }

    private void enviarEmail(String clienteId, String mensaje) {
        System.out.println("Enviando email a " + clienteId + ": " + mensaje);
    }

    private void enviarSMS(String clienteId, String mensaje) {
        System.out.println("Enviando SMS a " + clienteId + ": " + mensaje);
    }
}
