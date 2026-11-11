package co.ede.uniquindio.Parcial3Restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DetallePedidoTest {

    private static final double DELTA = 0.001;
    private PlatoFuerte plato;
    private DetallePedido detalle;

    @BeforeEach
    void setUp() {
        plato = new PlatoFuerte("P1", "Bandeja paisa", "Plato tipico", 20000, 30, 500, TipoPreparacion.ASADO, true);
        detalle = new DetallePedido(plato, 3);
    }

    @Test
    @DisplayName("El subtotal es el precio de venta por la cantidad")
    void subtotalNormal() {
        assertEquals(72000, detalle.calcularSubtotal(), DELTA);
    }

    @Test
    @DisplayName("Con cantidad 1 el subtotal es igual al precio de venta")
    void subtotalCantidadUno() {
        detalle.setCantidad(1);
        assertEquals(plato.calcularPrecioVenta(), detalle.calcularSubtotal(), DELTA);
    }

    @Test
    @DisplayName("Con cantidad cero el subtotal es cero")
    void subtotalCantidadCero() {
        detalle.setCantidad(0);
        assertEquals(0, detalle.calcularSubtotal(), DELTA);
    }

    @Test
    @DisplayName("Con cantidad negativa el subtotal es cero")
    void subtotalCantidadNegativa() {
        detalle.setCantidad(-2);
        assertEquals(0, detalle.calcularSubtotal(), DELTA);
    }

    @Test
    @DisplayName("Sin producto el subtotal es cero")
    void subtotalSinProducto() {
        detalle.setTheProducto(null);
        assertEquals(0, detalle.calcularSubtotal(), DELTA);
        assertTrue(detalle.toString().contains("producto=null"));
    }

    @Test
    @DisplayName("El subtotal usa el precio de venta de cada tipo de producto")
    void subtotalConOtrosProductos() {
        Bebida bebida = new Bebida("B1", "Limonada", "", 5000, 5, 350, 4, true, true);
        detalle.setTheProducto(bebida);
        detalle.setCantidad(2);
        assertEquals(17000, detalle.calcularSubtotal(), DELTA);
        Postre postre = new Postre("D1", "Flan", "", 8000, 10, 150, 4, true);
        detalle.setTheProducto(postre);
        assertEquals(19200, detalle.calcularSubtotal(), DELTA);
    }

    @Test
    @DisplayName("Los getters devuelven el producto y la cantidad, y toString los muestra")
    void gettersYToString() {
        assertSame(plato, detalle.getTheProducto());
        assertEquals(3, detalle.getCantidad());
        assertEquals("DetallePedido{producto=P1, cantidad=3}", detalle.toString());
    }
}
