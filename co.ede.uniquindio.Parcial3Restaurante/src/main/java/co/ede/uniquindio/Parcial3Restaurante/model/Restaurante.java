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
        // Empresas externas contratadas por el restaurante
        this.listEmpresasDomicilio.add(new RapidoExpress());
        this.listEmpresasDomicilio.add(new EntregaSegura());
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

    // ================= REGISTROS =================

    /**
     * Registra un producto en el menu.
     * @return true si se registro; false si ya existe un producto con ese codigo.
     */
    public boolean registrarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (buscarProducto(producto.getCodigo()) != null) {
            return false;
        }
        listProductos.add(producto);
        return true;
    }

    /**
     * Registra un cliente.
     * @return true si se registro; false si ya existe un cliente con ese documento.
     */
    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (buscarCliente(cliente.getDocumento()) != null) {
            return false;
        }
        listClientes.add(cliente);
        return true;
    }

    /**
     * Registra una empresa de domicilio.
     * @return true si se registro; false si ya existe una empresa con ese nombre.
     */
    public boolean registrarEmpresaDomicilio(EmpresaDomicilio empresa) {
        if (empresa == null) {
            throw new IllegalArgumentException("La empresa no puede ser nula.");
        }
        if (buscarEmpresaDomicilio(empresa.getNombre()) != null) {
            return false;
        }
        listEmpresasDomicilio.add(empresa);
        return true;
    }

    /**
     * Registra un pedido (en estado PENDIENTE) para un cliente registrado.
     * @throws ElementoDuplicadoException    si el codigo del pedido ya existe.
     * @throws ElementoNoEncontradoException si el cliente no esta registrado.
     */
    public void registrarPedido(Pedido pedido)
            throws ElementoDuplicadoException, ElementoNoEncontradoException {
        if (pedido == null || pedido.getTheCliente() == null) {
            throw new IllegalArgumentException("El pedido y su cliente no pueden ser nulos.");
        }
        if (buscarPedido(pedido.getCodigo()) != null) {
            throw new ElementoDuplicadoException("Pedido", pedido.getCodigo());
        }
        // El pedido queda asociado al cliente registrado (misma instancia),
        // aunque se haya creado con otro objeto con el mismo documento.
        Cliente clienteRegistrado = obtenerCliente(pedido.getTheCliente().getDocumento());
        pedido.setTheCliente(clienteRegistrado);
        listPedidos.add(pedido);
    }

    // ================= ELIMINACION =================

    /**
     * Elimina un producto del menu.
     * @throws ElementoNoEncontradoException si el producto no existe.
     * @throws ElementoEnUsoException        si algun pedido incluye el producto.
     */
    public void eliminarProducto(String codigo)
            throws ElementoNoEncontradoException, ElementoEnUsoException {
        Producto producto = obtenerProducto(codigo);
        for (Pedido pedido : listPedidos) {
            for (DetallePedido detalle : pedido.getListDetalles()) {
                if (detalle.getTheProducto().getCodigo().equals(codigo)) {
                    throw new ElementoEnUsoException("Producto", codigo,
                            "esta incluido en el pedido '" + pedido.getCodigo() + "'");
                }
            }
        }
        listProductos.remove(producto);
    }

    /**
     * Elimina un cliente registrado.
     * @throws ElementoNoEncontradoException si el cliente no existe.
     * @throws ElementoEnUsoException        si el cliente tiene pedidos.
     */
    public void eliminarCliente(String documento)
            throws ElementoNoEncontradoException, ElementoEnUsoException {
        Cliente cliente = obtenerCliente(documento);
        for (Pedido pedido : listPedidos) {
            if (pedido.getTheCliente() != null
                    && pedido.getTheCliente().getDocumento().equals(documento)) {
                throw new ElementoEnUsoException("Cliente", documento,
                        "tiene el pedido '" + pedido.getCodigo() + "'");
            }
        }
        listClientes.remove(cliente);
    }

    // ================= ARMADO DEL PEDIDO =================

    /**
     * Agrega un producto del menu al pedido. Si el producto ya estaba en el
     * pedido, suma la cantidad. La regla de estado la valida el propio
     * {@link Pedido#agregarDetalle(Producto, int)}.
     * @throws ElementoNoEncontradoException    si el pedido o el producto no existen.
     * @throws EstadoPedidoInvalidoException    si el pedido no esta PENDIENTE.
     * @throws CantidadInvalidaException        si la cantidad no es mayor que cero.
     * @throws ProductoNoCumpleCalidadException si el producto no cumple calidad.
     */
    public void agregarProductoAPedido(String codigoPedido, String codigoProducto, int cantidad)
            throws ElementoNoEncontradoException, EstadoPedidoInvalidoException,
            CantidadInvalidaException, ProductoNoCumpleCalidadException {
        Pedido pedido = obtenerPedido(codigoPedido);
        Producto producto = obtenerProducto(codigoProducto);
        if (cantidad <= 0) {
            throw new CantidadInvalidaException(cantidad);
        }
        if (!producto.cumpleCondicionesCalidad()) {
            throw new ProductoNoCumpleCalidadException(producto.getCodigo(), producto.getNombre());
        }
        pedido.agregarDetalle(producto, cantidad);
    }

    /**
     * Solicita el domicilio a la empresa indicada. Las reglas (estado,
     * modalidad y aceptacion) las valida {@link Pedido#solicitarDomicilio}.
     * @throws ElementoNoEncontradoException si el pedido o la empresa no existen.
     * @throws EstadoPedidoInvalidoException si el pedido no esta PENDIENTE.
     * @throws PedidoInvalidoException       si el pedido no es a domicilio.
     * @throws DomicilioNoAceptadoException  si la empresa no acepta el recorrido.
     */
    public void solicitarDomicilio(String codigoPedido, String nombreEmpresa, double distanciaKm)
            throws ElementoNoEncontradoException, EstadoPedidoInvalidoException,
            PedidoInvalidoException, DomicilioNoAceptadoException {
        Pedido pedido = obtenerPedido(codigoPedido);
        EmpresaDomicilio empresa = obtenerEmpresaDomicilio(nombreEmpresa);
        pedido.solicitarDomicilio(empresa, distanciaKm);
    }

    // ================= CICLO DE VIDA DEL PEDIDO =================

    /**
     * Verifica lo que solo el restaurante puede comprobar con sus listas:
     * que el cliente este registrado y que los productos pertenezcan al menu.
     * El resto de reglas (estado, cantidades, calidad, domicilio) las valida
     * el propio {@link Pedido#confirmar()}.
     */
    private void validarContraRestaurante(Pedido pedido)
            throws ElementoNoEncontradoException, PedidoInvalidoException {
        if (pedido.getTheCliente() == null) {
            throw new PedidoInvalidoException("El pedido '" + pedido.getCodigo() + "' no tiene cliente.");
        }
        obtenerCliente(pedido.getTheCliente().getDocumento());
        for (DetallePedido detalle : pedido.getListDetalles()) {
            if (detalle.getTheProducto() != null) {
                obtenerProducto(detalle.getTheProducto().getCodigo());
            }
        }
    }

    /**
     * Confirma un pedido PENDIENTE que cumpla todas las validaciones y lo
     * agrega al historial del cliente (sin duplicados).
     * @throws RestauranteException (o una subclase) con el motivo del rechazo.
     */
    public void confirmarPedido(String codigoPedido) throws RestauranteException {
        Pedido pedido = obtenerPedido(codigoPedido);
        validarContraRestaurante(pedido);
        pedido.confirmar();
        obtenerCliente(pedido.getTheCliente().getDocumento()).agregarPedido(pedido);
    }

    /**
     * Marca como ENTREGADO un pedido CONFIRMADO.
     * @throws ElementoNoEncontradoException si el pedido no existe.
     * @throws EstadoPedidoInvalidoException si el pedido no esta CONFIRMADO.
     */
    public void entregarPedido(String codigoPedido)
            throws ElementoNoEncontradoException, EstadoPedidoInvalidoException {
        obtenerPedido(codigoPedido).entregar();
    }

    /**
     * Cancela un pedido PENDIENTE o CONFIRMADO.
     * @throws ElementoNoEncontradoException si el pedido no existe.
     * @throws EstadoPedidoInvalidoException si el pedido ya fue entregado o cancelado.
     */
    public void cancelarPedido(String codigoPedido)
            throws ElementoNoEncontradoException, EstadoPedidoInvalidoException {
        obtenerPedido(codigoPedido).cancelar();
    }

    // ================= BUSQUEDAS =================

    public Producto buscarProducto(String codigo) {
        for (Producto producto : listProductos) {
            if (producto.getCodigo().equals(codigo)) {
                return producto;
            }
        }
        return null;
    }

    public Cliente buscarCliente(String documento) {
        for (Cliente cliente : listClientes) {
            if (cliente.getDocumento().equals(documento)) {
                return cliente;
            }
        }
        return null;
    }

    public Pedido buscarPedido(String codigo) {
        for (Pedido pedido : listPedidos) {
            if (pedido.getCodigo().equals(codigo)) {
                return pedido;
            }
        }
        return null;
    }

    public EmpresaDomicilio buscarEmpresaDomicilio(String nombre) {
        for (EmpresaDomicilio empresa : listEmpresasDomicilio) {
            if (empresa.getNombre().equalsIgnoreCase(nombre)) {
                return empresa;
            }
        }
        return null;
    }

    // Versiones de busqueda que lanzan excepcion cuando no hay resultado
    private Producto obtenerProducto(String codigo) throws ElementoNoEncontradoException {
        Producto producto = buscarProducto(codigo);
        if (producto == null) {
            throw new ElementoNoEncontradoException("Producto", codigo);
        }
        return producto;
    }

    private Cliente obtenerCliente(String documento) throws ElementoNoEncontradoException {
        Cliente cliente = buscarCliente(documento);
        if (cliente == null) {
            throw new ElementoNoEncontradoException("Cliente", documento);
        }
        return cliente;
    }

    private Pedido obtenerPedido(String codigo) throws ElementoNoEncontradoException {
        Pedido pedido = buscarPedido(codigo);
        if (pedido == null) {
            throw new ElementoNoEncontradoException("Pedido", codigo);
        }
        return pedido;
    }

    private EmpresaDomicilio obtenerEmpresaDomicilio(String nombre) throws ElementoNoEncontradoException {
        EmpresaDomicilio empresa = buscarEmpresaDomicilio(nombre);
        if (empresa == null) {
            throw new ElementoNoEncontradoException("Empresa de domicilio", nombre);
        }
        return empresa;
    }

    // ================= CONSULTAS =================

    /**
     * Valor total de un pedido (productos + domicilio).
     * @throws ElementoNoEncontradoException si el pedido no existe.
     */
    public double calcularValorTotalPedido(String codigoPedido) throws ElementoNoEncontradoException {
        return obtenerPedido(codigoPedido).calcularTotal();
    }

    /**
     * Pedidos confirmados de un cliente (incluye los que luego se entregaron
     * o cancelaron).
     * @throws ElementoNoEncontradoException si el cliente no existe.
     */
    public List<Pedido> consultarHistorialCliente(String documento) throws ElementoNoEncontradoException {
        return new ArrayList<>(obtenerCliente(documento).getListPedidos());
    }

    /** Productos que cumplen las condiciones de calidad. */
    public List<Producto> consultarProductosDisponibles() {
        List<Producto> disponibles = new ArrayList<>();
        for (Producto producto : listProductos) {
            if (producto.cumpleCondicionesCalidad()) {
                disponibles.add(producto);
            }
        }
        return disponibles;
    }

    /** Productos que NO cumplen las condiciones de calidad. */
    public List<Producto> consultarProductosNoDisponibles() {
        List<Producto> noDisponibles = new ArrayList<>();
        for (Producto producto : listProductos) {
            if (!producto.cumpleCondicionesCalidad()) {
                noDisponibles.add(producto);
            }
        }
        return noDisponibles;
    }

    // ================= INGRESOS =================

    /** Ingresos propios del restaurante: solo productos, sin domicilios. */
    public double calcularIngresosRestaurante() {
        return ReporteRestaurante.generar(listPedidos).ingresosRestaurante();
    }

    /** Total de domicilios, diferenciado de los ingresos propios. */
    public double calcularIngresosDomicilios() {
        return ReporteRestaurante.generar(listPedidos).ingresosDomicilios();
    }

    /**
     * Producto con mayor cantidad de unidades vendidas.
     * @return null si no hay ventas.
     */
    public Producto consultarProductoMasVendido() {
        return ReporteRestaurante.generar(listPedidos).productoMasVendido();
    }

    /**
     * Categoria (PlatoFuerte, Bebida o Postre) con mayores ingresos por
     * productos.
     * @return null si no hay ventas.
     */
    public String consultarCategoriaMayoresIngresos() {
        return ReporteRestaurante.generar(listPedidos).categoriaMayoresIngresos();
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