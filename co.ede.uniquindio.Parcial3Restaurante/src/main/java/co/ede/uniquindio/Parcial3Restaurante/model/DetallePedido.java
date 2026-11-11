package co.ede.uniquindio.Parcial3Restaurante.model;

public class DetallePedido {

    private int cantidad;

    // Relaciones
    private Producto theProducto;

    public DetallePedido(Producto theProducto, int cantidad) {
        this.theProducto = theProducto;
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getTheProducto() {
        return theProducto;
    }

    public void setTheProducto(Producto theProducto) {
        this.theProducto = theProducto;
    }

    @Override
    public String toString() {
        return "DetallePedido{" + "producto=" + (theProducto != null ? theProducto.getCodigo() : null)
                + ", cantidad=" + cantidad + "}";
    }
}
