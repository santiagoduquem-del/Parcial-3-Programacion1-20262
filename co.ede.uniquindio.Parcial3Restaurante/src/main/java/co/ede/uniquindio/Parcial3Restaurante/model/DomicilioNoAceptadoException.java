package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Se lanza cuando una empresa de domicilio no acepta el recorrido, o cuando
 * un pedido a domicilio no tiene ninguna empresa que haya aceptado.
 */
public class DomicilioNoAceptadoException extends RestauranteException {

    public DomicilioNoAceptadoException(String mensaje) {
        super(mensaje);
    }

    public DomicilioNoAceptadoException(String empresa, double distanciaKm) {
        super("La empresa '" + empresa + "' no acepta el recorrido de " + distanciaKm + " km.");
    }
}