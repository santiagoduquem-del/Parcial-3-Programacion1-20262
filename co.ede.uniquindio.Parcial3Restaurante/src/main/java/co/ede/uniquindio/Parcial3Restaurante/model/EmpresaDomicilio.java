package co.ede.uniquindio.Parcial3Restaurante.model;

public abstract class EmpresaDomicilio {

    private String nombre;
    private double tarifaBase;
    private double tarifaKm;
    private double distanciaMaxima;

    public EmpresaDomicilio(String nombre, double tarifaBase, double tarifaKm, double distanciaMaxima) {
        this.nombre = nombre;
        this.tarifaBase = tarifaBase;
        this.tarifaKm = tarifaKm;
        this.distanciaMaxima = distanciaMaxima;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getTarifaBase() {
        return tarifaBase;
    }

    public void setTarifaBase(double tarifaBase) {
        this.tarifaBase = tarifaBase;
    }

    public double getTarifaKm() {
        return tarifaKm;
    }

    public void setTarifaKm(double tarifaKm) {
        this.tarifaKm = tarifaKm;
    }

    public double getDistanciaMaxima() {
        return distanciaMaxima;
    }

    public void setDistanciaMaxima(double distanciaMaxima) {
        this.distanciaMaxima = distanciaMaxima;
    }

    @Override
    public String toString() {
        return "nombre=" + nombre
                + ", tarifaBase=" + tarifaBase
                + ", tarifaKm=" + tarifaKm
                + ", distanciaMaxima=" + distanciaMaxima;
    }
}
