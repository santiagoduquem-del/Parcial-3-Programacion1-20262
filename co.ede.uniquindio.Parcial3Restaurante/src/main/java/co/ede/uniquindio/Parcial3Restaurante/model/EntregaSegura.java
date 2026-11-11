package co.ede.uniquindio.Parcial3Restaurante.model;

public class EntregaSegura extends EmpresaDomicilio {

    private double recargoDistancia;
    private double umbralRecargoKm;

    public EntregaSegura() {
        super("EntregaSegura", 7000, 1000, 20);
        this.recargoDistancia = 3000;
        this.umbralRecargoKm = 5;
    }

    public double getRecargoDistancia() {
        return recargoDistancia;
    }

    public void setRecargoDistancia(double recargoDistancia) {
        this.recargoDistancia = recargoDistancia;
    }

    public double getUmbralRecargoKm() {
        return umbralRecargoKm;
    }

    public void setUmbralRecargoKm(double umbralRecargoKm) {
        this.umbralRecargoKm = umbralRecargoKm;
    }

    @Override
    public String toString() {
        return "EntregaSegura{" + super.toString() + ", recargoDistancia=" + recargoDistancia
                + ", umbralRecargoKm=" + umbralRecargoKm + "}";
    }
}
