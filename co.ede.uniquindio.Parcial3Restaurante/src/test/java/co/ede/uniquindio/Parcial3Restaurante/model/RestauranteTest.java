package co.ede.uniquindio.Parcial3Restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteTest {

    private static final double DELTA = 0.001;
    private static final LocalDate HOY = LocalDate.of(2026, 11, 11);

    private Restaurante restaurante;
    private PlatoFuerte plato;          // precio de venta 24.000
    private Bebida bebida;              // precio de venta 5.500
    private Postre postre;              // precio de venta 9.600
    private PlatoFuerte platoSinCalidad;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante("El Buen Comer", "900123456-7", "Calle 10 # 5-20");
        plato = new PlatoFuerte("P1", "Bandeja paisa", "Plato tipico", 20000, 30, 500, TipoPreparacion.ASADO, true);
        bebida = new Bebida("B1", "Limonada", "Natural", 5000, 5, 350, 4, true, false);
        postre = new Postre("D1", "Flan", "De caramelo", 8000, 10, 150, 4, true);
        platoSinCalidad = new PlatoFuerte("P2", "Mojarra", "Frita", 18000, 25, 400, TipoPreparacion.FRITO, false);
        cliente = new Cliente("1001", "Ana Gomez", "3001234567", "ana@correo.com", "Calle 1");
        restaurante.registrarProducto(plato);
        restaurante.registrarProducto(bebida);
        restaurante.registrarProducto(postre);
        restaurante.registrarProducto(platoSinCalidad);
        restaurante.registrarCliente(cliente);
    }

    /** Registra un pedido PENDIENTE del cliente de prueba. */
    private Pedido crearPedido(String codigo, ModalidadEntrega modalidad) throws RestauranteException {
        Pedido pedido = new Pedido(codigo, HOY, modalidad, cliente);
        restaurante.registrarPedido(pedido);
        return pedido;
    }

    /** Pedido de consumo en restaurante confirmado: 2 platos + 1 bebida = 53.500. */
    private Pedido crearPedidoConfirmado(String codigo) throws RestauranteException {
        Pedido pedido = crearPedido(codigo, ModalidadEntrega.CONSUMO_RESTAURANTE);
        restaurante.agregarProductoAPedido(codigo, "P1", 2);
        restaurante.agregarProductoAPedido(codigo, "B1", 1);
        restaurante.confirmarPedido(codigo);
        return pedido;
    }

    /** Pedido a domicilio con 1 plato y RapidoExpress a 3 km (domicilio 9.500), sin confirmar. */
    private Pedido crearPedidoDomicilio(String codigo) throws RestauranteException {
        Pedido pedido = crearPedido(codigo, ModalidadEntrega.DOMICILIO);
        restaurante.agregarProductoAPedido(codigo, "P1", 1);
        restaurante.solicitarDomicilio(codigo, "RapidoExpress", 3);
        return pedido;
    }

    // ================= Pruebas base =================

    @Test
    @DisplayName("registrarProducto agrega un producto nuevo al menu")
    void registrarProducto() {
        Postre nuevo = new Postre("D2", "Brownie", "", 6000, 20, 120, 22, false);
        assertTrue(restaurante.registrarProducto(nuevo));
        assertEquals(5, restaurante.getListProductos().size());
    }

    @Test
    @DisplayName("registrarProducto rechaza un codigo repetido")
    void registrarProductoDuplicado() {
        Bebida repetida = new Bebida("P1", "Otra", "", 1000, 1, 200, 4, true, false);
        assertFalse(restaurante.registrarProducto(repetida));
        assertEquals(4, restaurante.getListProductos().size());
    }

    @Test
    @DisplayName("registrarCliente rechaza un documento repetido")
    void registrarClienteDuplicado() {
        Cliente repetido = new Cliente("1001", "Otra persona", "", "", "");
        assertFalse(restaurante.registrarCliente(repetido));
        assertEquals(1, restaurante.getListClientes().size());
    }

    @Test
    @DisplayName("registrarPedido con un cliente no registrado lanza ElementoNoEncontradoException")
    void registrarPedidoClienteNoRegistrado() {
        Cliente desconocido = new Cliente("9999", "Desconocido", "", "", "");
        Pedido pedido = new Pedido("PE1", HOY, ModalidadEntrega.CONSUMO_RESTAURANTE, desconocido);
        assertThrows(ElementoNoEncontradoException.class, () -> restaurante.registrarPedido(pedido));
        assertTrue(restaurante.getListPedidos().isEmpty());
    }

    @Test
    @DisplayName("registrarPedido con un codigo repetido lanza ElementoDuplicadoException")
    void registrarPedidoDuplicado() throws RestauranteException {
        crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        Pedido repetido = new Pedido("PE1", HOY, ModalidadEntrega.DOMICILIO, cliente);
        assertThrows(ElementoDuplicadoException.class, () -> restaurante.registrarPedido(repetido));
        assertEquals(1, restaurante.getListPedidos().size());
    }

    // ================= Registros (ampliadas) =================

    @Test
    @DisplayName("registrarCliente agrega un cliente nuevo")
    void registrarCliente() {
        assertTrue(restaurante.registrarCliente(new Cliente("2002", "Luis Perez", "", "", "")));
        assertEquals(2, restaurante.getListClientes().size());
    }

    @Test
    @DisplayName("Registrar producto, cliente, empresa o pedido nulos lanza IllegalArgumentException")
    void registrarNulos() {
        assertThrows(IllegalArgumentException.class, () -> restaurante.registrarProducto(null));
        assertThrows(IllegalArgumentException.class, () -> restaurante.registrarCliente(null));
        assertThrows(IllegalArgumentException.class, () -> restaurante.registrarEmpresaDomicilio(null));
        assertThrows(IllegalArgumentException.class, () -> restaurante.registrarPedido(null));
        Pedido sinCliente = new Pedido("PX", HOY, ModalidadEntrega.CONSUMO_RESTAURANTE, null);
        assertThrows(IllegalArgumentException.class, () -> restaurante.registrarPedido(sinCliente));
    }

    @Test
    @DisplayName("registrarPedido asocia el pedido a la instancia del cliente registrado")
    void registrarPedidoAsociaClienteRegistrado() throws RestauranteException {
        Cliente copia = new Cliente("1001", "Ana", "", "", "");
        Pedido pedido = new Pedido("PE1", HOY, ModalidadEntrega.CONSUMO_RESTAURANTE, copia);
        restaurante.registrarPedido(pedido);
        assertSame(cliente, pedido.getTheCliente());
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
    }

    @Test
    @DisplayName("registrarEmpresaDomicilio agrega una empresa nueva y rechaza un nombre repetido")
    void registrarEmpresaDomicilio() {
        assertFalse(restaurante.registrarEmpresaDomicilio(new RapidoExpress()));
        RapidoExpress otra = new RapidoExpress();
        otra.setNombre("Mensajeros Ya");
        assertTrue(restaurante.registrarEmpresaDomicilio(otra));
        assertEquals(3, restaurante.getListEmpresasDomicilio().size());
    }

    // ================= Busquedas =================

    @Test
    @DisplayName("buscarProducto devuelve el producto por codigo o null si no existe")
    void buscarProducto() {
        assertSame(plato, restaurante.buscarProducto("P1"));
        assertNull(restaurante.buscarProducto("ZZ"));
    }

    @Test
    @DisplayName("buscarCliente devuelve el cliente por documento o null si no existe")
    void buscarCliente() {
        assertSame(cliente, restaurante.buscarCliente("1001"));
        assertNull(restaurante.buscarCliente("0000"));
    }

    @Test
    @DisplayName("buscarPedido devuelve el pedido por codigo o null si no existe")
    void buscarPedido() throws RestauranteException {
        Pedido pedido = crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        assertSame(pedido, restaurante.buscarPedido("PE1"));
        assertNull(restaurante.buscarPedido("PE2"));
    }

    @Test
    @DisplayName("buscarEmpresaDomicilio ignora mayusculas y devuelve null si no existe")
    void buscarEmpresaDomicilio() {
        assertNotNull(restaurante.buscarEmpresaDomicilio("rapidoexpress"));
        assertNotNull(restaurante.buscarEmpresaDomicilio("ENTREGASEGURA"));
        assertNull(restaurante.buscarEmpresaDomicilio("Otra"));
    }

    // ================= Eliminacion y actualizacion =================

    @Test
    @DisplayName("eliminarProducto quita del menu un producto que no esta en pedidos")
    void eliminarProducto() throws RestauranteException {
        restaurante.eliminarProducto("D1");
        assertNull(restaurante.buscarProducto("D1"));
        assertEquals(3, restaurante.getListProductos().size());
    }

    @Test
    @DisplayName("eliminarProducto con un codigo inexistente lanza ElementoNoEncontradoException")
    void eliminarProductoInexistente() {
        assertThrows(ElementoNoEncontradoException.class, () -> restaurante.eliminarProducto("ZZ"));
    }

    @Test
    @DisplayName("eliminarProducto de un producto incluido en un pedido lanza ElementoEnUsoException")
    void eliminarProductoEnUso() throws RestauranteException {
        crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        restaurante.agregarProductoAPedido("PE1", "P1", 1);
        assertThrows(ElementoEnUsoException.class, () -> restaurante.eliminarProducto("P1"));
        assertNotNull(restaurante.buscarProducto("P1"));
    }

    @Test
    @DisplayName("eliminarCliente quita un cliente sin pedidos")
    void eliminarCliente() throws RestauranteException {
        restaurante.registrarCliente(new Cliente("2002", "Luis Perez", "", "", ""));
        restaurante.eliminarCliente("2002");
        assertNull(restaurante.buscarCliente("2002"));
        assertEquals(1, restaurante.getListClientes().size());
    }

    @Test
    @DisplayName("eliminarCliente con un documento inexistente lanza ElementoNoEncontradoException")
    void eliminarClienteInexistente() {
        assertThrows(ElementoNoEncontradoException.class, () -> restaurante.eliminarCliente("0000"));
    }

    @Test
    @DisplayName("eliminarCliente de un cliente con pedidos lanza ElementoEnUsoException")
    void eliminarClienteEnUso() throws RestauranteException {
        crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        assertThrows(ElementoEnUsoException.class, () -> restaurante.eliminarCliente("1001"));
        assertNotNull(restaurante.buscarCliente("1001"));
    }

    @Test
    @DisplayName("Actualizar un producto buscado cambia sus datos y su disponibilidad en el menu")
    void actualizarProducto() {
        Producto encontrado = restaurante.buscarProducto("P2");
        assertFalse(restaurante.consultarProductosDisponibles().contains(encontrado));
        ((PlatoFuerte) encontrado).setCoccionAprobada(true);
        encontrado.setPrecioBase(15000);
        assertTrue(restaurante.consultarProductosDisponibles().contains(encontrado));
        assertEquals(18000, restaurante.buscarProducto("P2").calcularPrecioVenta(), DELTA);
    }

    @Test
    @DisplayName("Actualizar un cliente buscado cambia sus datos en el restaurante")
    void actualizarCliente() {
        Cliente encontrado = restaurante.buscarCliente("1001");
        encontrado.setTelefono("3110000000");
        encontrado.setDireccionResidencia("Avenida 3");
        assertEquals("3110000000", restaurante.buscarCliente("1001").getTelefono());
        assertEquals("Avenida 3", restaurante.buscarCliente("1001").getDireccionResidencia());
    }

    // ================= agregarProductoAPedido =================

    @Test
    @DisplayName("agregarProductoAPedido agrega el producto y acumula la cantidad si se repite")
    void agregarProductoAPedido() throws RestauranteException {
        Pedido pedido = crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        restaurante.agregarProductoAPedido("PE1", "P1", 2);
        restaurante.agregarProductoAPedido("PE1", "P1", 1);
        restaurante.agregarProductoAPedido("PE1", "B1", 1);
        assertEquals(2, pedido.getListDetalles().size());
        assertEquals(3, pedido.getListDetalles().get(0).getCantidad());
    }

    @Test
    @DisplayName("agregarProductoAPedido con pedido o producto inexistente lanza ElementoNoEncontradoException")
    void agregarProductoAPedidoInexistentes() throws RestauranteException {
        crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        assertThrows(ElementoNoEncontradoException.class, () -> restaurante.agregarProductoAPedido("NO", "P1", 1));
        assertThrows(ElementoNoEncontradoException.class, () -> restaurante.agregarProductoAPedido("PE1", "NO", 1));
    }

    @Test
    @DisplayName("agregarProductoAPedido con cantidad cero o negativa lanza CantidadInvalidaException")
    void agregarProductoAPedidoCantidadInvalida() throws RestauranteException {
        Pedido pedido = crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        assertThrows(CantidadInvalidaException.class, () -> restaurante.agregarProductoAPedido("PE1", "P1", 0));
        assertThrows(CantidadInvalidaException.class, () -> restaurante.agregarProductoAPedido("PE1", "P1", -3));
        assertTrue(pedido.getListDetalles().isEmpty());
    }

    @Test
    @DisplayName("agregarProductoAPedido con producto sin calidad lanza ProductoNoCumpleCalidadException")
    void agregarProductoAPedidoSinCalidad() throws RestauranteException {
        crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        assertThrows(ProductoNoCumpleCalidadException.class,
                () -> restaurante.agregarProductoAPedido("PE1", "P2", 1));
    }

    @Test
    @DisplayName("agregarProductoAPedido a un pedido no PENDIENTE lanza EstadoPedidoInvalidoException")
    void agregarProductoAPedidoConfirmado() throws RestauranteException {
        crearPedidoConfirmado("PE1");
        assertThrows(EstadoPedidoInvalidoException.class,
                () -> restaurante.agregarProductoAPedido("PE1", "D1", 1));
    }

    // ================= solicitarDomicilio =================

    @Test
    @DisplayName("solicitarDomicilio registra la distancia y la empresa que acepta")
    void solicitarDomicilio() throws RestauranteException {
        Pedido pedido = crearPedidoDomicilio("PD1");
        assertEquals(3, pedido.getDistanciaKm(), DELTA);
        assertTrue(pedido.isDomicilioAceptado());
        assertEquals("RapidoExpress", pedido.getTheEmpresaDomicilio().getNombre());
    }

    @Test
    @DisplayName("solicitarDomicilio con una distancia que la empresa rechaza lanza DomicilioNoAceptadoException")
    void solicitarDomicilioRechazado() throws RestauranteException {
        Pedido pedido = crearPedido("PD1", ModalidadEntrega.DOMICILIO);
        assertThrows(DomicilioNoAceptadoException.class,
                () -> restaurante.solicitarDomicilio("PD1", "RapidoExpress", 15));
        assertFalse(pedido.isDomicilioAceptado());
        // Con otra empresa que si cubre esa distancia, se acepta
        restaurante.solicitarDomicilio("PD1", "EntregaSegura", 15);
        assertTrue(pedido.isDomicilioAceptado());
    }

    @Test
    @DisplayName("solicitarDomicilio para un pedido de consumo en restaurante lanza PedidoInvalidoException")
    void solicitarDomicilioPedidoNoDomicilio() throws RestauranteException {
        crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        assertThrows(PedidoInvalidoException.class,
                () -> restaurante.solicitarDomicilio("PE1", "RapidoExpress", 3));
    }

    @Test
    @DisplayName("solicitarDomicilio con pedido o empresa inexistente lanza ElementoNoEncontradoException")
    void solicitarDomicilioInexistentes() throws RestauranteException {
        crearPedido("PD1", ModalidadEntrega.DOMICILIO);
        assertThrows(ElementoNoEncontradoException.class,
                () -> restaurante.solicitarDomicilio("NO", "RapidoExpress", 3));
        assertThrows(ElementoNoEncontradoException.class,
                () -> restaurante.solicitarDomicilio("PD1", "Desconocida", 3));
    }

    @Test
    @DisplayName("solicitarDomicilio para un pedido no PENDIENTE lanza EstadoPedidoInvalidoException")
    void solicitarDomicilioPedidoCancelado() throws RestauranteException {
        crearPedido("PD1", ModalidadEntrega.DOMICILIO);
        restaurante.cancelarPedido("PD1");
        assertThrows(EstadoPedidoInvalidoException.class,
                () -> restaurante.solicitarDomicilio("PD1", "RapidoExpress", 3));
    }

    // ================= confirmarPedido =================

    @Test
    @DisplayName("confirmarPedido confirma el pedido y lo agrega al historial del cliente")
    void confirmarPedido() throws RestauranteException {
        Pedido pedido = crearPedidoConfirmado("PE1");
        assertEquals(EstadoPedido.CONFIRMADO, pedido.getEstado());
        assertSame(pedido, cliente.buscarPedido("PE1"));
    }

    @Test
    @DisplayName("confirmarPedido de un pedido a domicilio con empresa aceptada lo confirma")
    void confirmarPedidoDomicilio() throws RestauranteException {
        Pedido pedido = crearPedidoDomicilio("PD1");
        restaurante.confirmarPedido("PD1");
        assertEquals(EstadoPedido.CONFIRMADO, pedido.getEstado());
    }

    @Test
    @DisplayName("confirmarPedido sin productos lanza PedidoInvalidoException")
    void confirmarPedidoSinProductos() throws RestauranteException {
        crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        assertThrows(PedidoInvalidoException.class, () -> restaurante.confirmarPedido("PE1"));
        assertTrue(cliente.getListPedidos().isEmpty());
    }

    @Test
    @DisplayName("confirmarPedido a domicilio sin empresa aceptada lanza DomicilioNoAceptadoException")
    void confirmarPedidoDomicilioSinEmpresa() throws RestauranteException {
        crearPedido("PD1", ModalidadEntrega.DOMICILIO);
        restaurante.agregarProductoAPedido("PD1", "P1", 1);
        assertThrows(DomicilioNoAceptadoException.class, () -> restaurante.confirmarPedido("PD1"));
    }

    @Test
    @DisplayName("confirmarPedido de un pedido inexistente lanza ElementoNoEncontradoException")
    void confirmarPedidoInexistente() {
        assertThrows(ElementoNoEncontradoException.class, () -> restaurante.confirmarPedido("NO"));
    }

    @Test
    @DisplayName("confirmarPedido dos veces lanza EstadoPedidoInvalidoException y no duplica el historial")
    void confirmarPedidoDosVeces() throws RestauranteException {
        crearPedidoConfirmado("PE1");
        assertThrows(EstadoPedidoInvalidoException.class, () -> restaurante.confirmarPedido("PE1"));
        assertEquals(1, cliente.getListPedidos().size());
    }

    @Test
    @DisplayName("confirmarPedido con un producto que ya no esta en el menu lanza ElementoNoEncontradoException")
    void confirmarPedidoProductoFueraDelMenu() throws RestauranteException {
        crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        restaurante.agregarProductoAPedido("PE1", "B1", 1);
        restaurante.getListProductos().remove(bebida);
        assertThrows(ElementoNoEncontradoException.class, () -> restaurante.confirmarPedido("PE1"));
    }

    @Test
    @DisplayName("confirmarPedido con un producto que perdio la calidad lanza ProductoNoCumpleCalidadException")
    void confirmarPedidoProductoPerdioCalidad() throws RestauranteException {
        crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        restaurante.agregarProductoAPedido("PE1", "D1", 1);
        postre.setTemperaturaAlmacenamiento(12);
        assertThrows(ProductoNoCumpleCalidadException.class, () -> restaurante.confirmarPedido("PE1"));
    }

    // ================= entregarPedido y cancelarPedido =================

    @Test
    @DisplayName("entregarPedido marca como ENTREGADO un pedido CONFIRMADO")
    void entregarPedido() throws RestauranteException {
        Pedido pedido = crearPedidoConfirmado("PE1");
        restaurante.entregarPedido("PE1");
        assertEquals(EstadoPedido.ENTREGADO, pedido.getEstado());
    }

    @Test
    @DisplayName("entregarPedido de un pedido PENDIENTE o inexistente lanza excepcion")
    void entregarPedidoErrores() throws RestauranteException {
        crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        assertThrows(EstadoPedidoInvalidoException.class, () -> restaurante.entregarPedido("PE1"));
        assertThrows(ElementoNoEncontradoException.class, () -> restaurante.entregarPedido("NO"));
    }

    @Test
    @DisplayName("cancelarPedido cancela un pedido PENDIENTE y uno CONFIRMADO")
    void cancelarPedido() throws RestauranteException {
        Pedido pendiente = crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        Pedido confirmado = crearPedidoConfirmado("PE2");
        restaurante.cancelarPedido("PE1");
        restaurante.cancelarPedido("PE2");
        assertEquals(EstadoPedido.CANCELADO, pendiente.getEstado());
        assertEquals(EstadoPedido.CANCELADO, confirmado.getEstado());
    }

    @Test
    @DisplayName("cancelarPedido de un pedido ENTREGADO o inexistente lanza excepcion")
    void cancelarPedidoErrores() throws RestauranteException {
        crearPedidoConfirmado("PE1");
        restaurante.entregarPedido("PE1");
        assertThrows(EstadoPedidoInvalidoException.class, () -> restaurante.cancelarPedido("PE1"));
        assertThrows(ElementoNoEncontradoException.class, () -> restaurante.cancelarPedido("NO"));
    }

    // ================= Consultas =================

    @Test
    @DisplayName("calcularValorTotalPedido suma productos para consumo en restaurante")
    void calcularValorTotalPedidoConsumo() throws RestauranteException {
        crearPedidoConfirmado("PE1");
        assertEquals(53500, restaurante.calcularValorTotalPedido("PE1"), DELTA);
    }

    @Test
    @DisplayName("calcularValorTotalPedido suma productos y domicilio para pedidos a domicilio")
    void calcularValorTotalPedidoDomicilio() throws RestauranteException {
        crearPedidoDomicilio("PD1");
        assertEquals(24000 + 9500, restaurante.calcularValorTotalPedido("PD1"), DELTA);
    }

    @Test
    @DisplayName("calcularValorTotalPedido de un pedido inexistente lanza ElementoNoEncontradoException")
    void calcularValorTotalPedidoInexistente() {
        assertThrows(ElementoNoEncontradoException.class, () -> restaurante.calcularValorTotalPedido("NO"));
    }

    @Test
    @DisplayName("consultarHistorialCliente devuelve solo los pedidos confirmados del cliente")
    void consultarHistorialCliente() throws RestauranteException {
        assertTrue(restaurante.consultarHistorialCliente("1001").isEmpty());
        crearPedidoConfirmado("PE1");
        crearPedido("PE2", ModalidadEntrega.CONSUMO_RESTAURANTE); // pendiente: no entra
        List<Pedido> historial = restaurante.consultarHistorialCliente("1001");
        assertEquals(1, historial.size());
        assertEquals("PE1", historial.get(0).getCodigo());
    }

    @Test
    @DisplayName("consultarHistorialCliente devuelve una copia que no altera el historial real")
    void consultarHistorialClienteCopia() throws RestauranteException {
        crearPedidoConfirmado("PE1");
        restaurante.consultarHistorialCliente("1001").clear();
        assertEquals(1, restaurante.consultarHistorialCliente("1001").size());
    }

    @Test
    @DisplayName("consultarHistorialCliente de un cliente inexistente lanza ElementoNoEncontradoException")
    void consultarHistorialClienteInexistente() {
        assertThrows(ElementoNoEncontradoException.class, () -> restaurante.consultarHistorialCliente("0000"));
    }

    @Test
    @DisplayName("consultarProductosDisponibles devuelve solo los productos que cumplen calidad")
    void consultarProductosDisponibles() {
        List<Producto> disponibles = restaurante.consultarProductosDisponibles();
        assertEquals(3, disponibles.size());
        assertTrue(disponibles.contains(plato));
        assertTrue(disponibles.contains(bebida));
        assertTrue(disponibles.contains(postre));
        assertFalse(disponibles.contains(platoSinCalidad));
    }

    @Test
    @DisplayName("consultarProductosNoDisponibles devuelve solo los productos que no cumplen calidad")
    void consultarProductosNoDisponibles() {
        List<Producto> noDisponibles = restaurante.consultarProductosNoDisponibles();
        assertEquals(1, noDisponibles.size());
        assertSame(platoSinCalidad, noDisponibles.get(0));
    }

    @Test
    @DisplayName("Las consultas de disponibilidad devuelven listas vacias si no hay productos")
    void consultarDisponibilidadSinProductos() {
        restaurante.setListProductos(new ArrayList<>());
        assertTrue(restaurante.consultarProductosDisponibles().isEmpty());
        assertTrue(restaurante.consultarProductosNoDisponibles().isEmpty());
    }

    // ================= Ingresos =================

    @Test
    @DisplayName("calcularIngresosRestaurante cuenta solo productos de pedidos confirmados o entregados")
    void calcularIngresosRestaurante() throws RestauranteException {
        assertEquals(0, restaurante.calcularIngresosRestaurante(), DELTA);
        crearPedidoConfirmado("PE1");                  // 53.500 confirmado
        crearPedidoConfirmado("PE2");                  // 53.500 entregado
        restaurante.entregarPedido("PE2");
        crearPedidoConfirmado("PE3");                  // cancelado: no cuenta
        restaurante.cancelarPedido("PE3");
        crearPedidoDomicilio("PD1");                   // pendiente: no cuenta
        assertEquals(107000, restaurante.calcularIngresosRestaurante(), DELTA);
    }

    @Test
    @DisplayName("calcularIngresosDomicilios suma solo el domicilio de pedidos confirmados o entregados")
    void calcularIngresosDomicilios() throws RestauranteException {
        assertEquals(0, restaurante.calcularIngresosDomicilios(), DELTA);
        crearPedidoDomicilio("PD1");
        assertEquals(0, restaurante.calcularIngresosDomicilios(), DELTA); // aun pendiente
        restaurante.confirmarPedido("PD1");
        assertEquals(9500, restaurante.calcularIngresosDomicilios(), DELTA);
        crearPedidoConfirmado("PE1");                  // consumo en restaurante: no suma domicilio
        assertEquals(9500, restaurante.calcularIngresosDomicilios(), DELTA);
        // Los ingresos del restaurante no incluyen el domicilio
        assertEquals(24000 + 53500, restaurante.calcularIngresosRestaurante(), DELTA);
    }

    @Test
    @DisplayName("calcularIngresosDomicilios no cuenta pedidos a domicilio cancelados")
    void calcularIngresosDomiciliosCancelado() throws RestauranteException {
        crearPedidoDomicilio("PD1");
        restaurante.confirmarPedido("PD1");
        restaurante.cancelarPedido("PD1");
        assertEquals(0, restaurante.calcularIngresosDomicilios(), DELTA);
    }

    @Test
    @DisplayName("consultarProductoMasVendido devuelve null si no hay ventas")
    void productoMasVendidoSinVentas() throws RestauranteException {
        assertNull(restaurante.consultarProductoMasVendido());
        crearPedido("PE1", ModalidadEntrega.CONSUMO_RESTAURANTE);
        restaurante.agregarProductoAPedido("PE1", "B1", 5); // pendiente: no es venta
        assertNull(restaurante.consultarProductoMasVendido());
    }

    @Test
    @DisplayName("consultarProductoMasVendido devuelve el producto con mas unidades vendidas")
    void productoMasVendido() throws RestauranteException {
        crearPedidoConfirmado("PE1");                  // P1 x2, B1 x1
        crearPedido("PE2", ModalidadEntrega.CONSUMO_RESTAURANTE);
        restaurante.agregarProductoAPedido("PE2", "B1", 2);
        restaurante.confirmarPedido("PE2");            // B1 total 3
        assertSame(bebida, restaurante.consultarProductoMasVendido());
    }

    @Test
    @DisplayName("consultarProductoMasVendido ignora los pedidos cancelados")
    void productoMasVendidoIgnoraCancelados() throws RestauranteException {
        crearPedidoConfirmado("PE1");                  // P1 x2, B1 x1
        crearPedido("PE2", ModalidadEntrega.CONSUMO_RESTAURANTE);
        restaurante.agregarProductoAPedido("PE2", "B1", 10);
        restaurante.confirmarPedido("PE2");
        restaurante.cancelarPedido("PE2");
        assertSame(plato, restaurante.consultarProductoMasVendido());
    }

    @Test
    @DisplayName("consultarCategoriaMayoresIngresos devuelve null sin ventas y la categoria con mas ingresos")
    void categoriaMayoresIngresos() throws RestauranteException {
        assertNull(restaurante.consultarCategoriaMayoresIngresos());
        crearPedidoConfirmado("PE1");                  // platos 48.000, bebidas 5.500
        assertEquals("PlatoFuerte", restaurante.consultarCategoriaMayoresIngresos());
        crearPedido("PE2", ModalidadEntrega.CONSUMO_RESTAURANTE);
        restaurante.agregarProductoAPedido("PE2", "D1", 6); // postres 57.600
        restaurante.confirmarPedido("PE2");
        assertEquals("Postre", restaurante.consultarCategoriaMayoresIngresos());
        crearPedido("PE3", ModalidadEntrega.CONSUMO_RESTAURANTE);
        restaurante.agregarProductoAPedido("PE3", "B1", 20); // bebidas 115.500
        restaurante.confirmarPedido("PE3");
        assertEquals("Bebida", restaurante.consultarCategoriaMayoresIngresos());
    }

    // ================= Getters, setters y toString =================

    @Test
    @DisplayName("El constructor registra RapidoExpress y EntregaSegura y los setters actualizan los datos")
    void constructorGettersYSetters() {
        assertEquals("El Buen Comer", restaurante.getNombreComercial());
        assertEquals("900123456-7", restaurante.getNit());
        assertEquals("Calle 10 # 5-20", restaurante.getDireccion());
        assertEquals(2, restaurante.getListEmpresasDomicilio().size());

        restaurante.setNombreComercial("El Buen Comer 2");
        restaurante.setNit("800");
        restaurante.setDireccion("Carrera 1");
        restaurante.setListClientes(new ArrayList<>());
        restaurante.setListPedidos(new ArrayList<>());
        restaurante.setListEmpresasDomicilio(new ArrayList<>());
        assertEquals("El Buen Comer 2", restaurante.getNombreComercial());
        assertEquals("800", restaurante.getNit());
        assertEquals("Carrera 1", restaurante.getDireccion());
        assertTrue(restaurante.getListClientes().isEmpty());
        assertTrue(restaurante.getListPedidos().isEmpty());
        assertNull(restaurante.buscarEmpresaDomicilio("RapidoExpress"));
    }

    @Test
    @DisplayName("toString del restaurante muestra los totales")
    void toStringRestaurante() {
        String texto = restaurante.toString();
        assertTrue(texto.contains("nombreComercial=El Buen Comer"));
        assertTrue(texto.contains("totalProductos=4"));
        assertTrue(texto.contains("totalClientes=1"));
    }
}
