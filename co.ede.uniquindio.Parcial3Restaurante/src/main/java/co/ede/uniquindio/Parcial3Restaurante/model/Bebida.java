package co.ede.uniquindio.Parcial3Restaurante.model;

public class Bebida extends Producto {

    private double volumenMl;
    private double temperaturaServicio;
    private boolean fria;
    private boolean requierePreparacionEspecial;

    public Bebida(String codigo, String nombre, String descripcion, double precioBase, int tiempoPreparacion, double volumenMl, double temperaturaServicio, boolean fria, boolean requierePreparacionEspecial) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacion);
        this.volumenMl = volumenMl;
        this.temperaturaServicio = temperaturaServicio;
        this.fria = fria;
        this.requierePreparacionEspecial = requierePreparacionEspecial;
    }

    public double getVolumenMl() {
        return volumenMl;
    }

    public void setVolumenMl(double volumenMl) {
        this.volumenMl = volumenMl;
    }

    public double getTemperaturaServicio() {
        return temperaturaServicio;
    }

    public void setTemperaturaServicio(double temperaturaServicio) {
        this.temperaturaServicio = temperaturaServicio;
    }

    public boolean isFria() {
        return fria;
    }

    public void setFria(boolean fria) {
        this.fria = fria;
    }

    public boolean isRequierePreparacionEspecial() {
        return requierePreparacionEspecial;
    }

    public void setRequierePreparacionEspecial(boolean requierePreparacionEspecial) {
        this.requierePreparacionEspecial = requierePreparacionEspecial;
    }

    @Override
    public String toString() {
        return "Bebida{" + super.toString() + ", volumenMl=" + volumenMl
                + ", temperaturaServicio=" + temperaturaServicio
                + ", fria=" + fria
                + ", requierePreparacionEspecial=" + requierePreparacionEspecial + "}";
    }
}
