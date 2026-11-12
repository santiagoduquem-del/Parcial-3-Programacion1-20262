package co.ede.uniquindio.Parcial3Restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReporteRestauranteTest {

    private static final double DELTA = 0.001;
    private static final LocalDate HOY = LocalDate.of(2026, 11, 11);

    private Cliente cliente;
    private PlatoFuerte plato;          // precio de venta 24.000
    private Bebida bebida;              // precio de venta 5.500
    private Bebida bebidaEspecial;      // precio de venta 3.000 (base 0 + preparacion especial)
    private Postre postre;              // precio de venta 9.600
    private List<Pedido> pedidos;
    private int consecutivo;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("1001", "Ana Gomez", "3001234567", "ana@correo.com", "Calle 1");
        plato = new PlatoFuerte("P1", "Bandeja paisa", "Plato tipico", 20000, 30, 500, TipoPreparacion.ASADO, true);
        bebida = new Bebida("B1", "Limonada", "Natural", 5000, 5, 350, 4, true, false);
        bebidaEspecial = new Bebida("B2", "Coctel", "De la casa", 0, 5, 350, 4, true, true);
        postre = new Postre("D1", "Flan", "De caramelo", 8000, 10, 150, 4, true);
        pedidos = new ArrayList<>();
        consecutivo = 0;
    }

    /** Pedido PENDIENTE de consumo en restaurante con los productos indicados. */
    private Pedido pedidoPendiente(Object... productoYCantidad) throws RestauranteException {
        Pedido pedido = new Pedido("PE" + (++consecutivo), HOY, ModalidadEntrega.CONSUMO_RESTAURANTE, cliente);
        for (int i = 0; i < productoYCantidad.length; i += 2) {
            pedido.agregarDetalle((Producto) productoYCantidad[i], (Integer) productoYCantidad[i + 1]);
        }
        pedidos.add(pedido);
        return pedido;
    }

    /** Pedido CONFIRMADO de consumo en restaurante con los productos indicados. */
    private Pedido pedidoConfirmado(Object... productoYCantidad) throws RestauranteException {
        Pedido pedido = pedidoPendiente(productoYCantidad);
        pedido.confirmar();
        return pedido;
    }

    @Test
    @DisplayName("Sin pedidos ni ventas el reporte queda en cero y sin producto ni categoria")
    void sinVentas() throws RestauranteException {
        ReporteRestaurante vacio = ReporteRestaurante.generar(pedidos);
        assertEquals(0, vacio.ingresosRestaurante(), DELTA);
        assertEquals(0, vacio.ingresosDomicilios(), DELTA);
        assertEquals(0, vacio.totalFacturado(), DELTA);
        assertNull(vacio.productoMasVendido());
        assertNull(vacio.categoriaMayoresIngresos());

        pedidoPendiente(plato, 3);                     // pendiente: aun no es venta
        ReporteRestaurante soloPendientes = ReporteRestaurante.generar(pedidos);
        assertEquals(0, soloPendientes.ingresosRestaurante(), DELTA);
        assertNull(soloPendientes.productoMasVendido());
        assertNull(soloPendientes.categoriaMayoresIngresos());
    }

    @Test
    @DisplayName("Cuenta los pedidos CONFIRMADOS y ENTREGADOS como ventas")
    void cuentaConfirmadosYEntregados() throws RestauranteException {
        pedidoConfirmado(plato, 2, bebida, 1);         // 53.500 confirmado
        pedidoConfirmado(postre, 1).entregar();        // 9.600 entregado
        ReporteRestaurante reporte = ReporteRestaurante.generar(pedidos);
        assertEquals(53500 + 9600, reporte.ingresosRestaurante(), DELTA);
    }

    @Test
    @DisplayName("Excluye los pedidos CANCELADOS y PENDIENTES")
    void excluyeCanceladosYPendientes() throws RestauranteException {
        pedidoConfirmado(plato, 1);                    // 24.000 cuenta
        pedidoConfirmado(bebida, 10).cancelar();       // cancelado tras confirmar
        pedidoPendiente(postre, 10).cancelar();        // cancelado estando pendiente
        pedidoPendiente(bebida, 20);                   // pendiente
        ReporteRestaurante reporte = ReporteRestaurante.generar(pedidos);
        assertEquals(24000, reporte.ingresosRestaurante(), DELTA);
        assertSame(plato, reporte.productoMasVendido());
        assertEquals("PlatoFuerte", reporte.categoriaMayoresIngresos());
    }

    @Test
    @DisplayName("Los ingresos de domicilio se calculan aparte de los ingresos del restaurante")
    void ingresosDomicilioAparte() throws RestauranteException {
        Pedido domicilio = new Pedido("PD1", HOY, ModalidadEntrega.DOMICILIO, cliente);
        domicilio.agregarDetalle(plato, 1);            // 24.000
        domicilio.solicitarDomicilio(new RapidoExpress(), 3); // domicilio 9.500
        domicilio.confirmar();
        pedidos.add(domicilio);

        Pedido domicilioCancelado = new Pedido("PD2", HOY, ModalidadEntrega.DOMICILIO, cliente);
        domicilioCancelado.agregarDetalle(plato, 1);
        domicilioCancelado.solicitarDomicilio(new EntregaSegura(), 6);
        domicilioCancelado.confirmar();
        domicilioCancelado.cancelar();                 // no cuenta
        pedidos.add(domicilioCancelado);

        pedidoConfirmado(bebida, 1);                   // 5.500 sin domicilio

        ReporteRestaurante reporte = ReporteRestaurante.generar(pedidos);
        assertEquals(24000 + 5500, reporte.ingresosRestaurante(), DELTA);
        assertEquals(9500, reporte.ingresosDomicilios(), DELTA);
    }

    @Test
    @DisplayName("El producto mas vendido es el de mas unidades y se devuelve el mismo objeto del detalle")
    void productoMasVendido() throws RestauranteException {
        pedidoConfirmado(plato, 2, bebida, 1);
        pedidoConfirmado(bebida, 2);                   // bebida suma 3 unidades
        ReporteRestaurante reporte = ReporteRestaurante.generar(pedidos);
        assertSame(bebida, reporte.productoMasVendido());
        // El plato genera mas ingresos, pero se miden unidades, no dinero
        assertEquals("PlatoFuerte", reporte.categoriaMayoresIngresos());
    }

    @Test
    @DisplayName("La categoria con mayores ingresos se decide por dinero y no por unidades")
    void categoriaPorIngresos() throws RestauranteException {
        pedidoConfirmado(plato, 1, postre, 3);         // platos 24.000, postres 28.800
        assertEquals("Postre", ReporteRestaurante.generar(pedidos).categoriaMayoresIngresos());
        pedidoConfirmado(bebida, 10);                  // bebidas 55.000
        assertEquals("Bebida", ReporteRestaurante.generar(pedidos).categoriaMayoresIngresos());
    }

    @Test
    @DisplayName("En empate de ingresos gana PlatoFuerte, luego Bebida, luego Postre")
    void categoriaConEmpate() throws RestauranteException {
        // PlatoFuerte 48.000 = Postre 48.000 -> PlatoFuerte
        pedidoConfirmado(plato, 2, postre, 5);
        assertEquals("PlatoFuerte", ReporteRestaurante.generar(pedidos).categoriaMayoresIngresos());

        // Bebida 48.000 = Postre 48.000 (sin platos) -> Bebida
        pedidos.clear();
        pedidoConfirmado(bebidaEspecial, 16, postre, 5);
        assertEquals("Bebida", ReporteRestaurante.generar(pedidos).categoriaMayoresIngresos());

        // Empate de las tres en 48.000 -> PlatoFuerte
        pedidos.clear();
        pedidoConfirmado(plato, 2, bebidaEspecial, 16, postre, 5);
        assertEquals("PlatoFuerte", ReporteRestaurante.generar(pedidos).categoriaMayoresIngresos());
    }

    @Test
    @DisplayName("totalFacturado suma los ingresos del restaurante y los de domicilios")
    void totalFacturado() throws RestauranteException {
        assertEquals(1500 + 2500, new ReporteRestaurante(1500, 2500, null, null).totalFacturado(), DELTA);

        Pedido domicilio = new Pedido("PD1", HOY, ModalidadEntrega.DOMICILIO, cliente);
        domicilio.agregarDetalle(plato, 1);
        domicilio.solicitarDomicilio(new RapidoExpress(), 3);
        domicilio.confirmar();
        pedidos.add(domicilio);
        pedidoConfirmado(bebida, 1);
        ReporteRestaurante reporte = ReporteRestaurante.generar(pedidos);
        assertEquals(24000 + 5500 + 9500, reporte.totalFacturado(), DELTA);
        assertEquals(reporte.ingresosRestaurante() + reporte.ingresosDomicilios(), reporte.totalFacturado(), DELTA);
    }

    private String prueba; // variable de prueba para que github deskopt lea los cambios
}
