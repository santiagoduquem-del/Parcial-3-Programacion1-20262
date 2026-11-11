package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Se lanza cuando se busca un producto, cliente, pedido o empresa de
 * domicilio que no existe en el restaurante.
 */
public class ElementoNoEncontradoException extends RestauranteException {

    /**
     * @param tipo          tipo de elemento ("Producto", "Cliente", "Pedido", ...)
     * @param identificador codigo, documento o nombre buscado
     */
    public ElementoNoEncontradoException(String tipo, String identificador) {
        super(tipo + " '" + identificador + "' no existe.");
    }
}