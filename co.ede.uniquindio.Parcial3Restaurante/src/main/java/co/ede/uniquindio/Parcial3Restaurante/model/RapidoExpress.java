package co.ede.uniquindio.Parcial3Restaurante.model;

public class RapidoExpress extends EmpresaDomicilio {

    public RapidoExpress() {
        super("RapidoExpress", 5000, 1500, 12);
    }

    /**
     * Tarifa base + tarifa por kilometro x distancia.
     */
    @Override
    public double calcularCostoDomicilio(double distanciaKm) {
        return getTarifaBase() + getTarifaKm() * distanciaKm;
    }

    @Override
    public String toString() {
        return "RapidoExpress{" + super.toString() + "}";
    }
}