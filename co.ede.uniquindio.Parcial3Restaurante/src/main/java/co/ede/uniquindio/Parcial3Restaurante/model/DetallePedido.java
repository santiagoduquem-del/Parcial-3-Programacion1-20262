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

    /**
     * Subtotal de la linea: precio de venta del producto x cantidad.
     */
    public double calcularSubtotal() {
        if (theProducto == null || cantidad <= 0) {
            return 0;
        }
        return theProducto.calcularPrecioVenta() * cantidad;
    }

    @Override
    public String toString() {
        return "DetallePedido{" + "producto=" + (theProducto != null ? theProducto.getCodigo() : null)
                + ", cantidad=" + cantidad + "}";
    }
}