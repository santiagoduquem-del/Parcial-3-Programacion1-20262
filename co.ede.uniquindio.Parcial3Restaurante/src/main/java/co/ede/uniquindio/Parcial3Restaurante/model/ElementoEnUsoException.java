package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Se lanza al eliminar un producto o un cliente que esta siendo usado
 * por algun pedido del restaurante.
 */
public class ElementoEnUsoException extends RestauranteException {

    /**
     * @param tipo          tipo de elemento ("Producto" o "Cliente")
     * @param identificador codigo o documento del elemento
     * @param motivo        razon por la que no se puede eliminar
     */
    public ElementoEnUsoException(String tipo, String identificador, String motivo) {
        super("No se puede eliminar el " + tipo.toLowerCase() + " '" + identificador + "' porque " + motivo + ".");
    }
}