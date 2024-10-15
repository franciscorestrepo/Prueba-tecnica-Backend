package com.pruebatecnica.pruebatecnica.service;

import com.pruebatecnica.pruebatecnica.dto.NotificacionDto;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    public void enviarNotificacion(NotificacionDto notificacion, String correo, String celular) {
        switch (notificacion.getTipo()) {
            case 1: // EMAIL
                enviarEmail(correo, notificacion.getMensaje());
                break;
            case 2: // SMS
                enviarSMS(celular, notificacion.getMensaje());
                break;
            default:
                throw new IllegalArgumentException("Tipo de notificación no válido: " + notificacion.getTipo());
        }
    }

    private void enviarEmail(String correo, String mensaje) {
        if (correo != null && !correo.isEmpty()) {
            System.out.println("Enviando email a " + correo + ": " + mensaje);
        } else {
            throw new RuntimeException("Correo no proporcionado para enviar la notificación.");
        }
    }

    private void enviarSMS(String celular, String mensaje) {
        if (celular != null && !celular.isEmpty()) {
            System.out.println("Enviando SMS al número " + celular + ": " + mensaje);
        } else {
            throw new RuntimeException("Número de celular no proporcionado para enviar la notificación.");
        }
    }
}