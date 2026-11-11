package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Se lanza cuando la cantidad solicitada de un producto no es mayor que cero.
 */
public class CantidadInvalidaException extends RestauranteException {

    public CantidadInvalidaException(int cantidad) {
        super("La cantidad debe ser mayor que cero (recibida: " + cantidad + ").");
    }
}