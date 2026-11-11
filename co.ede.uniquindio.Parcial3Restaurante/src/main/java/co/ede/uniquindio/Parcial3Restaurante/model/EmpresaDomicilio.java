package co.ede.uniquindio.Parcial3Restaurante.model;

public abstract class EmpresaDomicilio implements IServicioDomicilio {

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

    /**
     * Una empresa acepta el recorrido si la distancia es mayor que cero
     * y no supera su distancia maxima.
     */
    @Override
    public boolean aceptaDistancia(double distanciaKm) {
        return distanciaKm > 0 && distanciaKm <= distanciaMaxima;
    }

    /**
     * Procedimiento comun para solicitar el servicio a cualquier empresa.
     * Usa la distancia registrada en el pedido; si la empresa acepta, queda
     * asignada al pedido y este se marca como aceptado.
     */
    @Override
    public boolean solicitarServicio(Pedido pedido) {
        if (pedido == null || pedido.getModalidadEntrega() != ModalidadEntrega.DOMICILIO) {
            return false;
        }
        if (aceptaDistancia(pedido.getDistanciaKm())) {
            pedido.setTheEmpresaDomicilio(this);
            pedido.setDomicilioAceptado(true);
            return true;
        }
        pedido.setTheEmpresaDomicilio(null);
        pedido.setDomicilioAceptado(false);
        return false;
    }

    @Override
    public String toString() {
        return "nombre=" + nombre
                + ", tarifaBase=" + tarifaBase
                + ", tarifaKm=" + tarifaKm
                + ", distanciaMaxima=" + distanciaMaxima;
    }
}