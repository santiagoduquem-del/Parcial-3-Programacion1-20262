package co.ede.uniquindio.Parcial3Restaurante.model;

import java.util.ArrayList;
import java.util.List;

public abstract class EmpresaDomicilio {
    //Atributos de la clase EmpresaDomicilio
    private String nombre;
    private double tarifaBase;
    private double tarifaKm;
    private double distanciaMaxima;
    //Relaciones de la clase EmpresaDomicilio
    private List<Pedido> listPedidos;

    /**
     * Metodo constructor de la clase EmpresaDomicilio
     * @param nombre de la Empresa de Domicilio
     * @param tarifaBase de la Empresa de Domicilio
     * @param tarifaKm de la Empresa de Domicilio
     * @param distanciaMaxima de la Empresa de Domicilio
     */
    public EmpresaDomicilio(String nombre, double tarifaBase, double tarifaKm, double distanciaMaxima) {
        this.nombre = nombre;
        this.tarifaBase = tarifaBase;
        this.tarifaKm = tarifaKm;
        this.distanciaMaxima = distanciaMaxima;
        this.listPedidos = new ArrayList<>();
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

    public List<Pedido> getListPedidos() {
        return listPedidos;
    }

    public void setListPedidos(List<Pedido> listPedidos) {
        this.listPedidos = listPedidos;
    }

    @Override
    public String toString() {
        return "nombre=" + nombre
                + ", tarifaBase=" + tarifaBase
                + ", tarifaKm=" + tarifaKm
                + ", distanciaMaxima=" + distanciaMaxima;
    }
}
