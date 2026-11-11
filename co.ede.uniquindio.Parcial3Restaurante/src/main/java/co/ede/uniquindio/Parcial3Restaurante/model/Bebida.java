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

    /**
     * Precio de venta: precio base + 10 %, mas $3.000 fijos si requiere
     * preparacion especial.
     */
    @Override
    public double calcularPrecioVenta() {
        double precio = getPrecioBase() * 1.10;
        if (requierePreparacionEspecial) {
            precio += 3000;
        }
        return precio;
    }

    /**
     * Calidad: volumen entre 150 y 1.500 ml; temperatura entre 0 y 10 C si es
     * fria, o entre 55 y 85 C si es caliente.
     */
    @Override
    public boolean cumpleCondicionesCalidad() {
        boolean volumenValido = volumenMl >= 150 && volumenMl <= 1500;
        boolean temperaturaValida;
        if (fria) {
            temperaturaValida = temperaturaServicio >= 0 && temperaturaServicio <= 10;
        } else {
            temperaturaValida = temperaturaServicio >= 55 && temperaturaServicio <= 85;
        }
        return volumenValido && temperaturaValida;
    }

    @Override
    public String toString() {
        return "Bebida{" + super.toString() + ", volumenMl=" + volumenMl
                + ", temperaturaServicio=" + temperaturaServicio
                + ", fria=" + fria
                + ", requierePreparacionEspecial=" + requierePreparacionEspecial + "}";
    }
}