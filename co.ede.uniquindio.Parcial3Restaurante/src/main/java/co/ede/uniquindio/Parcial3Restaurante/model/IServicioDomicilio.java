package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Contrato comun para las empresas externas de domicilio.
 * Permite solicitar el servicio con un procedimiento unico,
 * sin importar la empresa (RapidoExpress, EntregaSegura, etc.).
 */
public interface IServicioDomicilio {

    /**
     * @param distanciaKm distancia del recorrido en kilometros.
     * @return costo total del domicilio para esa distancia.
     */
    double calcularCostoDomicilio(double distanciaKm);

    /**
     * @param distanciaKm distancia del recorrido en kilometros.
     * @return true si la empresa acepta recorrer esa distancia.
     */
    boolean aceptaDistancia(double distanciaKm);

    /**
     * Procedimiento comun para solicitar el servicio: evalua la distancia
     * del pedido y registra si la empresa acepto o no la entrega.
     *
     * @param pedido pedido a domicilio que se desea entregar.
     * @return true si la empresa acepto el servicio.
     */
    boolean solicitarServicio(Pedido pedido);
}