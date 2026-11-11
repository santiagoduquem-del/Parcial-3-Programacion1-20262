package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Se lanza cuando un pedido no cumple una condicion general: no tiene
 * productos, o se pide domicilio para un pedido que no es a domicilio.
 */
public class PedidoInvalidoException extends RestauranteException {

    public PedidoInvalidoException(String mensaje) {
        super(mensaje);
    }
}