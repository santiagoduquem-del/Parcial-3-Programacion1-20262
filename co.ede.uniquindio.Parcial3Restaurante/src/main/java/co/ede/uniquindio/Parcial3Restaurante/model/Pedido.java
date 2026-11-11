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

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
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
