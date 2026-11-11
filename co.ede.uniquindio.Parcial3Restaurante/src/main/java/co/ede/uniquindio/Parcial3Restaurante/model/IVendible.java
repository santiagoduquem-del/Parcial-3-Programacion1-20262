package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Contrato para todo elemento que tenga un precio de venta
 * calculado a partir de su precio base y sus caracteristicas.
 */
public interface IVendible {

    /**
     * @return precio final de venta de una unidad.
     */
    double calcularPrecioVenta();
}