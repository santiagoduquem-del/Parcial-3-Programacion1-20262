package co.ede.uniquindio.Parcial3Restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private static final double DELTA = 0.001;
    private Cliente cliente;
    private PlatoFuerte plato;
    private Bebida bebida;
    private Pedido pedido;
    private Pedido pedidoDomicilio;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("1001", "Ana Gomez", "3001234567", "ana@correo.com", "Calle 1");
        plato = new PlatoFuerte("P1", "Bandeja paisa", "Plato tipico", 20000, 30, 500, TipoPreparacion.ASADO, true);
        bebida = new Bebida("B1", "Limonada", "Natural", 5000, 5, 350, 4, true, false);
        pedido = new Pedido("PE1", LocalDate.of(2026, 11, 11), ModalidadEntrega.CONSUMO_RESTAURANTE, cliente);
        pedidoDomicilio = new Pedido("PD1", LocalDate.of(2026, 11, 11), ModalidadEntrega.DOMICILIO, cliente);
    }

    // ---------- Pruebas base ----------

    @Test
    @DisplayName("Un pedido nuevo queda PENDIENTE, sin detalles y sin domicilio")
    void estadoInicial() {
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
        assertTrue(pedido.getListDetalles().isEmpty());
        assertFalse(pedido.isDomicilioAceptado());
        assertNull(pedido.getTheEmpresaDomicilio());
        assertEquals(0, pedido.getDistanciaKm(), DELTA);
    }

    @Test
    @DisplayName("agregarDetalle agrega una linea con el producto y la cantidad")
    void agregarDetalle() throws CantidadInvalidaException {
        pedido.agregarDetalle(plato, 2);
        assertEquals(1, pedido.getListDetalles().size());
        assertEquals(2, pedido.getListDetalles().get(0).getCantidad());
    }

    @Test
    @DisplayName("agregarDetalle suma la cantidad si el producto ya estaba en el pedido")
    void agregarDetalleAcumula() throws CantidadInvalidaException {
        pedido.agregarDetalle(plato, 2);
        pedido.agregarDetalle(plato, 3);
        assertEquals(1, pedido.getListDetalles().size());
        assertEquals(5, pedido.getListDetalles().get(0).getCantidad());
    }

    @Test
    @DisplayName("agregarDetalle con cantidad cero lanza CantidadInvalidaException")
    void agregarDetalleCantidadInvalida() {
        assertThrows(CantidadInvalidaException.class, () -> pedido.agregarDetalle(plato, 0));
        assertTrue(pedido.getListDetalles().isEmpty());
    }

    @Test
    @DisplayName("Confirmar un pedido sin productos lanza PedidoInvalidoException")
    void confirmarSinProductos() {
        assertThrows(PedidoInvalidoException.class, () -> pedido.confirmar());
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
    }

    // ---------- Pruebas ampliadas: calculos ----------

    @Test
    @DisplayName("agregarDetalle con producto nulo lanza IllegalArgumentException")
    void agregarDetalleProductoNulo() {
        assertThrows(IllegalArgumentException.class, () -> pedido.agregarDetalle(null, 1));
    }

    @Test
    @DisplayName("calcularValorProductos suma los subtotales de todas las lineas")
    void calcularValorProductos() throws CantidadInvalidaException {
        assertEquals(0, pedido.calcularValorProductos(), DELTA);
        pedido.agregarDetalle(plato, 2);   // 2 x 24.000
        pedido.agregarDetalle(bebida, 1);  // 1 x 5.500
        assertEquals(53500, pedido.calcularValorProductos(), DELTA);
    }

    @Test
    @DisplayName("calcularCostoDomicilio es cero para un pedido de consumo en el restaurante")
    void costoDomicilioConsumoRestaurante() {
        pedido.setDistanciaKm(3);
        pedido.setDomicilioAceptado(true);
        pedido.setTheEmpresaDomicilio(new RapidoExpress());
        assertEquals(0, pedido.calcularCostoDomicilio(), DELTA);
    }

    @Test
    @DisplayName("calcularCostoDomicilio es cero si ninguna empresa ha aceptado el domicilio")
    void costoDomicilioSinEmpresa() {
        pedidoDomicilio.setDistanciaKm(3);
        assertEquals(0, pedidoDomicilio.calcularCostoDomicilio(), DELTA);
    }

    @Test
    @DisplayName("calcularCostoDomicilio usa la tarifa de la empresa que acepto")
    void costoDomicilioConEmpresa() {
        pedidoDomicilio.setDistanciaKm(3);
        assertTrue(new RapidoExpress().solicitarServicio(pedidoDomicilio));
        assertEquals(9500, pedidoDomicilio.calcularCostoDomicilio(), DELTA);
        pedidoDomicilio.setDistanciaKm(6);
        assertTrue(new EntregaSegura().solicitarServicio(pedidoDomicilio));
        assertEquals(16000, pedidoDomicilio.calcularCostoDomicilio(), DELTA);
    }

    @Test
    @DisplayName("calcularTotal suma el valor de los productos y el domicilio")
    void calcularTotal() throws CantidadInvalidaException {
        pedidoDomicilio.agregarDetalle(plato, 1);
        pedidoDomicilio.setDistanciaKm(3);
        new RapidoExpress().solicitarServicio(pedidoDomicilio);
        assertEquals(24000 + 9500, pedidoDomicilio.calcularTotal(), DELTA);
    }

    // ---------- Pruebas ampliadas: confirmar ----------

    @Test
    @DisplayName("Confirmar un pedido valido de consumo en restaurante lo deja CONFIRMADO")
    void confirmarValido() throws RestauranteException {
        pedido.agregarDetalle(plato, 1);
        pedido.confirmar();
        assertEquals(EstadoPedido.CONFIRMADO, pedido.getEstado());
    }

    @Test
    @DisplayName("Confirmar un pedido a domicilio con empresa aceptada lo deja CONFIRMADO")
    void confirmarDomicilioValido() throws RestauranteException {
        pedidoDomicilio.agregarDetalle(plato, 1);
        pedidoDomicilio.setDistanciaKm(4);
        new RapidoExpress().solicitarServicio(pedidoDomicilio);
        pedidoDomicilio.confirmar();
        assertEquals(EstadoPedido.CONFIRMADO, pedidoDomicilio.getEstado());
    }

    @Test
    @DisplayName("Confirmar con un producto que no cumple calidad lanza ProductoNoCumpleCalidadException")
    void confirmarProductoSinCalidad() throws CantidadInvalidaException {
        plato.setCoccionAprobada(false);
        pedido.agregarDetalle(plato, 1);
        assertThrows(ProductoNoCumpleCalidadException.class, () -> pedido.confirmar());
    }

    @Test
    @DisplayName("Confirmar un pedido a domicilio sin empresa lanza DomicilioNoAceptadoException")
    void confirmarDomicilioSinEmpresa() throws CantidadInvalidaException {
        pedidoDomicilio.agregarDetalle(plato, 1);
        assertThrows(DomicilioNoAceptadoException.class, () -> pedidoDomicilio.confirmar());
    }

    @Test
    @DisplayName("Confirmar un pedido que no esta PENDIENTE lanza EstadoPedidoInvalidoException")
    void confirmarDosVeces() throws RestauranteException {
        pedido.agregarDetalle(plato, 1);
        pedido.confirmar();
        assertThrows(EstadoPedidoInvalidoException.class, () -> pedido.confirmar());
    }

    @Test
    @DisplayName("Confirmar con un detalle sin producto o con cantidad cero es rechazado")
    void confirmarDetallesInvalidos() {
        List<DetallePedido> sinProducto = new ArrayList<>();
        sinProducto.add(new DetallePedido(null, 1));
        pedido.setListDetalles(sinProducto);
        assertThrows(PedidoInvalidoException.class, () -> pedido.confirmar());

        List<DetallePedido> cantidadCero = new ArrayList<>();
        cantidadCero.add(new DetallePedido(plato, 0));
        pedido.setListDetalles(cantidadCero);
        assertThrows(CantidadInvalidaException.class, () -> pedido.confirmar());
    }

    // ---------- Pruebas ampliadas: entregar y cancelar ----------

    @Test
    @DisplayName("Entregar un pedido CONFIRMADO lo deja ENTREGADO")
    void entregarConfirmado() throws RestauranteException {
        pedido.agregarDetalle(plato, 1);
        pedido.confirmar();
        pedido.entregar();
        assertEquals(EstadoPedido.ENTREGADO, pedido.getEstado());
    }

    @Test
    @DisplayName("Entregar un pedido PENDIENTE lanza EstadoPedidoInvalidoException")
    void entregarPendiente() {
        assertThrows(EstadoPedidoInvalidoException.class, () -> pedido.entregar());
    }

    @Test
    @DisplayName("Cancelar un pedido PENDIENTE lo deja CANCELADO")
    void cancelarPendiente() throws EstadoPedidoInvalidoException {
        pedido.cancelar();
        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
    }

    @Test
    @DisplayName("Cancelar un pedido CONFIRMADO lo deja CANCELADO")
    void cancelarConfirmado() throws RestauranteException {
        pedido.agregarDetalle(plato, 1);
        pedido.confirmar();
        pedido.cancelar();
        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
    }

    @Test
    @DisplayName("No se puede cancelar un pedido ENTREGADO ni uno ya CANCELADO")
    void cancelarEntregadoOCancelado() throws RestauranteException {
        pedido.agregarDetalle(plato, 1);
        pedido.confirmar();
        pedido.entregar();
        assertThrows(EstadoPedidoInvalidoException.class, () -> pedido.cancelar());

        pedidoDomicilio.cancelar();
        assertThrows(EstadoPedidoInvalidoException.class, () -> pedidoDomicilio.cancelar());
    }

    @Test
    @DisplayName("Los getters y setters de Pedido guardan los valores y toString los resume")
    void gettersYSetters() {
        Cliente otro = new Cliente("2002", "Luis", "300", "luis@correo.com", "Calle 2");
        pedido.setCodigo("PE9");
        pedido.setFecha(LocalDate.of(2026, 12, 24));
        pedido.setModalidadEntrega(ModalidadEntrega.DOMICILIO);
        pedido.setEstado(EstadoPedido.CONFIRMADO);
        pedido.setTheCliente(otro);
        assertEquals("PE9", pedido.getCodigo());
        assertEquals(LocalDate.of(2026, 12, 24), pedido.getFecha());
        assertEquals(ModalidadEntrega.DOMICILIO, pedido.getModalidadEntrega());
        assertEquals(EstadoPedido.CONFIRMADO, pedido.getEstado());
        assertSame(otro, pedido.getTheCliente());
        String texto = pedido.toString();
        assertTrue(texto.contains("codigo=PE9"));
        assertTrue(texto.contains("cliente=2002"));
    }
}
