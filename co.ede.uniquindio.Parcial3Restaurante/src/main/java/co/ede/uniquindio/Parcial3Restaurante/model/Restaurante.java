package co.ede.uniquindio.Parcial3Restaurante.model;

import java.util.List;
import java.util.ArrayList;

public class Restaurante {

    private String nombreComercial;
    private String nit;
    private String direccion;

    // Relaciones
    private List<Producto> listProductos;
    private List<Cliente> listClientes;
    private List<Pedido> listPedidos;
    private List<EmpresaDomicilio> listEmpresasDomicilio;

    public Restaurante(String nombreComercial, String nit, String direccion) {
        this.nombreComercial = nombreComercial;
        this.nit = nit;
        this.direccion = direccion;
        this.listProductos = new ArrayList<>();
        this.listClientes = new ArrayList<>();
        this.listPedidos = new ArrayList<>();
        this.listEmpresasDomicilio = new ArrayList<>();
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Producto> getListProductos() {
        return listProductos;
    }

    public void setListProductos(List<Producto> listProductos) {
        this.listProductos = listProductos;
    }

    public List<Cliente> getListClientes() {
        return listClientes;
    }

    public void setListClientes(List<Cliente> listClientes) {
        this.listClientes = listClientes;
    }

    public List<Pedido> getListPedidos() {
        return listPedidos;
    }

    public void setListPedidos(List<Pedido> listPedidos) {
        this.listPedidos = listPedidos;
    }

    public List<EmpresaDomicilio> getListEmpresasDomicilio() {
        return listEmpresasDomicilio;
    }

    public void setListEmpresasDomicilio(List<EmpresaDomicilio> listEmpresasDomicilio) {
        this.listEmpresasDomicilio = listEmpresasDomicilio;
    }

    @Override
    public String toString() {
        return "Restaurante{" + "nombreComercial=" + nombreComercial
                + ", nit=" + nit
                + ", direccion=" + direccion
                + ", totalProductos=" + listProductos.size()
                + ", totalClientes=" + listClientes.size()
                + ", totalPedidos=" + listPedidos.size() + "}";
    }
}
