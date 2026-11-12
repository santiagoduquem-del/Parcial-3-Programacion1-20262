package co.ede.uniquindio.Parcial3Restaurante.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Pedido {

    private String codigo;
    private LocalDate fecha;
    private ModalidadEntrega modalidadEntrega;
    private EstadoPedido estado;
    private double distanciaKm;
    private boolean domicilioAceptado;

    // Relaciones
    private Cliente theCliente;
    private List<DetallePedido> listDetalles;
    private EmpresaDomicilio theEmpresaDomicilio;

    public Pedido(String codigo, LocalDate fecha, ModalidadEntrega modalidadEntrega, Cliente theCliente) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.modalidadEntrega = modalidadEntrega;
        this.theCliente = theCliente;
        this.listDetalles = new ArrayList<>();
        this.estado = EstadoPedido.PENDIENTE;
        this.distanciaKm = 0;
        this.domicilioAceptado = false;
        this.theEmpresaDomicilio = null;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public ModalidadEntrega getModalidadEntrega() {
        return modalidadEntrega;
    }

    public void setModalidadEntrega(ModalidadEntrega modalidadEntrega) {
        this.modalidadEntrega = modalidadEntrega;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public boolean isDomicilioAceptado() {
        return domicilioAceptado;
    }

    public void setDomicilioAceptado(boolean domicilioAceptado) {
        this.domicilioAceptado = domicilioAceptado;
    }

    public Cliente getTheCliente() {
        return theCliente;
    }

    public void setTheCliente(Cliente theCliente) {
        this.theCliente = theCliente;
    }

    public List<DetallePedido> getListDetalles() {
        return listDetalles;
    }

    public void setListDetalles(List<DetallePedido> listDetalles) {
        this.listDetalles = listDetalles;
    }

    public EmpresaDomicilio getTheEmpresaDomicilio() {
        return theEmpresaDomicilio;
    }

    public void setTheEmpresaDomicilio(EmpresaDomicilio theEmpresaDomicilio) {
        this.theEmpresaDomicilio = theEmpresaDomicilio;
    }

    /**
     * Agrega un producto al pedido. Si el producto ya estaba en el pedido,
     * suma la cantidad al detalle existente en lugar de duplicarlo.
     * @throws EstadoPedidoInvalidoException si el pedido no esta PENDIENTE.
     * @throws CantidadInvalidaException     si la cantidad no es mayor que cero.
     */
    public void agregarDetalle(Producto producto, int cantidad)
            throws CantidadInvalidaException, EstadoPedidoInvalidoException {
        if (estado != EstadoPedido.PENDIENTE) {
            throw new EstadoPedidoInvalidoException(codigo, estado.toString(), "modificar");
        }
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (cantidad <= 0) {
            throw new CantidadInvalidaException(cantidad);
        }
        for (DetallePedido detalle : listDetalles) {
            if (detalle.getTheProducto().getCodigo().equals(producto.getCodigo())) {
                detalle.setCantidad(detalle.getCantidad() + cantidad);
                return;
            }
        }
        listDetalles.add(new DetallePedido(producto, cantidad));
    }

    /**
     * Solicita el domicilio a la empresa indicada: registra la distancia y
     * deja que la empresa decida con su procedimiento comun.
     * @throws EstadoPedidoInvalidoException si el pedido no esta PENDIENTE.
     * @throws PedidoInvalidoException       si el pedido no es a domicilio.
     * @throws DomicilioNoAceptadoException  si la empresa no acepta el recorrido.
     */
    public void solicitarDomicilio(EmpresaDomicilio empresa, double distanciaKm)
            throws EstadoPedidoInvalidoException, PedidoInvalidoException, DomicilioNoAceptadoException {
        if (estado != EstadoPedido.PENDIENTE) {
            throw new EstadoPedidoInvalidoException(codigo, estado.toString(), "solicitar domicilio para");
        }
        if (modalidadEntrega != ModalidadEntrega.DOMICILIO) {
            throw new PedidoInvalidoException("El pedido '" + codigo + "' no es a domicilio.");
        }
        if (empresa == null) {
            throw new IllegalArgumentException("La empresa no puede ser nula.");
        }
        this.distanciaKm = distanciaKm;
        if (!empresa.solicitarServicio(this)) {
            throw new DomicilioNoAceptadoException(empresa.getNombre(), distanciaKm);
        }
    }

    /** Un pedido cuenta como venta si esta CONFIRMADO o ENTREGADO. */
    public boolean esVenta() {
        return estado == EstadoPedido.CONFIRMADO || estado == EstadoPedido.ENTREGADO;
    }

    /**
     * Valor de los productos del pedido, sin incluir el domicilio.
     * Sirve para separar los ingresos propios del restaurante.
     */
    public double calcularValorProductos() {
        double total = 0;
        for (DetallePedido detalle : listDetalles) {
            total += detalle.calcularSubtotal();
        }
        return total;
    }

    /**
     * Costo del domicilio: solo aplica si el pedido es a domicilio y una
     * empresa externa acepto la entrega.
     */
    public double calcularCostoDomicilio() {
        if (modalidadEntrega == ModalidadEntrega.DOMICILIO
                && domicilioAceptado && theEmpresaDomicilio != null) {
            return theEmpresaDomicilio.calcularCostoDomicilio(distanciaKm);
        }
        return 0;
    }

    /**
     * Valor total del pedido: productos + domicilio (cuando corresponda).
     */
    public double calcularTotal() {
        return calcularValorProductos() + calcularCostoDomicilio();
    }

    /**
     * Confirma el pedido. Debe estar PENDIENTE, tener productos con cantidad
     * mayor que cero que cumplan las condiciones de calidad y, si es a
     * domicilio, una empresa que haya aceptado la entrega.
     * @throws RestauranteException (o una subclase) con el motivo del rechazo.
     */
    public void confirmar() throws RestauranteException {
        if (estado != EstadoPedido.PENDIENTE) {
            throw new EstadoPedidoInvalidoException(codigo, estado.toString(), "confirmar");
        }
        if (listDetalles.isEmpty()) {
            throw new PedidoInvalidoException("El pedido '" + codigo + "' no tiene productos.");
        }
        for (DetallePedido detalle : listDetalles) {
            Producto producto = detalle.getTheProducto();
            if (producto == null) {
                throw new PedidoInvalidoException("El pedido '" + codigo + "' tiene un detalle sin producto.");
            }
            if (detalle.getCantidad() <= 0) {
                throw new CantidadInvalidaException(detalle.getCantidad());
            }
            if (!producto.cumpleCondicionesCalidad()) {
                throw new ProductoNoCumpleCalidadException(producto.getCodigo(), producto.getNombre());
            }
        }
        if (modalidadEntrega == ModalidadEntrega.DOMICILIO
                && (!domicilioAceptado || theEmpresaDomicilio == null)) {
            throw new DomicilioNoAceptadoException("Ninguna empresa de domicilio ha aceptado la entrega del pedido '"
                    + codigo + "'.");
        }
        estado = EstadoPedido.CONFIRMADO;
    }

    /**
     * Marca el pedido como ENTREGADO.
     * @throws EstadoPedidoInvalidoException si el pedido no esta CONFIRMADO.
     */
    public void entregar() throws EstadoPedidoInvalidoException {
        if (estado != EstadoPedido.CONFIRMADO) {
            throw new EstadoPedidoInvalidoException(codigo, estado.toString(), "entregar");
        }
        estado = EstadoPedido.ENTREGADO;
    }

    /**
     * Cancela el pedido (PENDIENTE o CONFIRMADO).
     * @throws EstadoPedidoInvalidoException si ya fue entregado o cancelado.
     */
    public void cancelar() throws EstadoPedidoInvalidoException {
        if (estado == EstadoPedido.ENTREGADO || estado == EstadoPedido.CANCELADO) {
            throw new EstadoPedidoInvalidoException(codigo, estado.toString(), "cancelar");
        }
        estado = EstadoPedido.CANCELADO;
    }

    @Override
    public String toString() {
        return "Pedido{" + "codigo=" + codigo
                + ", fecha=" + fecha
                + ", modalidadEntrega=" + modalidadEntrega
                + ", estado=" + estado
                + ", distanciaKm=" + distanciaKm
                + ", domicilioAceptado=" + domicilioAceptado
                + ", cliente=" + (theCliente != null ? theCliente.getDocumento() : null)
                + ", empresaDomicilio=" + (theEmpresaDomicilio != null ? theEmpresaDomicilio.getNombre() : null)
                + ", totalDetalles=" + listDetalles.size() + "}";
    }
}