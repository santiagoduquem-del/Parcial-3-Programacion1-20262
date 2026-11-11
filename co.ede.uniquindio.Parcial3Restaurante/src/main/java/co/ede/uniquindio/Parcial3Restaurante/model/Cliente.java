package co.ede.uniquindio.Parcial3Restaurante.model;

import java.util.List;
import java.util.ArrayList;

public class Cliente {

    private String documento;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String direccionResidencia;

    // Relaciones
    private List<Pedido> listPedidos;

    public Cliente(String documento, String nombreCompleto, String telefono, String correo, String direccionResidencia) {
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.direccionResidencia = direccionResidencia;
        this.listPedidos = new ArrayList<>();
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccionResidencia() {
        return direccionResidencia;
    }

    public void setDireccionResidencia(String direccionResidencia) {
        this.direccionResidencia = direccionResidencia;
    }

    public List<Pedido> getListPedidos() {
        return listPedidos;
    }

    public void setListPedidos(List<Pedido> listPedidos) {
        this.listPedidos = listPedidos;
    }

    @Override
    public String toString() {
        return "Cliente{" + "documento=" + documento
                + ", nombreCompleto=" + nombreCompleto
                + ", telefono=" + telefono
                + ", correo=" + correo
                + ", direccionResidencia=" + direccionResidencia
                + ", totalPedidos=" + listPedidos.size() + "}";
    }
}
