package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Se lanza al registrar un producto, cliente, pedido o empresa de domicilio
 * cuyo codigo o documento ya existe.
 */
public class ElementoDuplicadoException extends RestauranteException {

    /**
     * @param tipo          tipo de elemento ("Producto", "Cliente", "Pedido", ...)
     * @param identificador codigo o documento duplicado
     */
    public ElementoDuplicadoException(String tipo, String identificador) {
        super(tipo + " '" + identificador + "' ya esta registrado.");
    }
}