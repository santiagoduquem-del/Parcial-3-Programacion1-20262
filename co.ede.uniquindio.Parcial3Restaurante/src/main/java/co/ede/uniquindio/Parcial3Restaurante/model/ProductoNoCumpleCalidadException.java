package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Se lanza cuando un producto no cumple las condiciones de calidad y por
 * tanto no puede incluirse en un pedido confirmado.
 */
public class ProductoNoCumpleCalidadException extends RestauranteException {

    public ProductoNoCumpleCalidadException(String codigo, String nombre) {
        super("El producto '" + nombre + "' (" + codigo + ") no cumple las condiciones de calidad.");
    }
}