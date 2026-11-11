package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Contrato para los productos que deben superar controles de calidad
 * antes de poder incluirse en un pedido confirmado.
 */
public interface IVerificableCalidad {

    /**
     * @return true si el producto cumple todas las condiciones de calidad.
     */
    boolean cumpleCondicionesCalidad();
}