package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Se lanza cuando se intenta una operacion que no corresponde al estado
 * actual del pedido (por ejemplo, entregar un pedido cancelado).
 */
public class EstadoPedidoInvalidoException extends RestauranteException {

    /**
     * @param codigoPedido codigo del pedido
     * @param estadoActual estado en el que se encuentra
     * @param accion       operacion intentada ("confirmar", "entregar", ...)
     */
    public EstadoPedidoInvalidoException(String codigoPedido, String estadoActual, String accion) {
        super("No se puede " + accion + " el pedido '" + codigoPedido
                + "' porque su estado es " + estadoActual + ".");
    }
}