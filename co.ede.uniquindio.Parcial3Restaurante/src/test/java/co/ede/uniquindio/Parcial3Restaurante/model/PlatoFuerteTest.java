package co.ede.uniquindio.Parcial3Restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlatoFuerteTest {

    private static final double DELTA = 0.001;
    private PlatoFuerte plato;

    @BeforeEach
    void setUp() {
        plato = new PlatoFuerte("P1", "Bandeja paisa", "Plato tipico", 20000, 30, 500, TipoPreparacion.ASADO, true);
    }

    // ---------- Pruebas base ----------

    @Test
    @DisplayName("El precio de venta es el precio base mas el 20 %")
    void calcularPrecioVenta() {
        assertEquals(24000, plato.calcularPrecioVenta(), DELTA);
    }

    @Test
    @DisplayName("Un plato con peso, tiempo y coccion validos cumple calidad")
    void cumpleCalidadValido() {
        assertTrue(plato.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Un plato con peso fuera del rango no cumple calidad")
    void pesoFueraDeRango() {
        plato.setPesoGramos(150);
        assertFalse(plato.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Un plato con tiempo de preparacion fuera del rango no cumple calidad")
    void tiempoFueraDeRango() {
        plato.setTiempoPreparacion(90);
        assertFalse(plato.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Un plato sin coccion aprobada no cumple calidad")
    void coccionNoAprobada() {
        plato.setCoccionAprobada(false);
        assertFalse(plato.cumpleCondicionesCalidad());
    }

    // ---------- Pruebas ampliadas ----------

    @Test
    @DisplayName("Los limites de peso (200 y 900 g) y de tiempo (10 y 60 min) son validos")
    void limitesInclusivos() {
        plato.setPesoGramos(200);
        plato.setTiempoPreparacion(10);
        assertTrue(plato.cumpleCondicionesCalidad());
        plato.setPesoGramos(900);
        plato.setTiempoPreparacion(60);
        assertTrue(plato.cumpleCondicionesCalidad());
        plato.setPesoGramos(900.1);
        assertFalse(plato.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Los getters y setters de PlatoFuerte y Producto guardan los valores")
    void gettersYSetters() {
        plato.setCodigo("P9");
        plato.setNombre("Sancocho");
        plato.setDescripcion("Sopa");
        plato.setPrecioBase(10000);
        plato.setTipoPreparacion(TipoPreparacion.HORNEADO);
        assertEquals("P9", plato.getCodigo());
        assertEquals("Sancocho", plato.getNombre());
        assertEquals("Sopa", plato.getDescripcion());
        assertEquals(10000, plato.getPrecioBase(), DELTA);
        assertEquals(500, plato.getPesoGramos(), DELTA);
        assertEquals(30, plato.getTiempoPreparacion());
        assertEquals(TipoPreparacion.HORNEADO, plato.getTipoPreparacion());
        assertTrue(plato.isCoccionAprobada());
        assertEquals(12000, plato.calcularPrecioVenta(), DELTA);
    }

    @Test
    @DisplayName("toString incluye el tipo y el codigo del plato")
    void toStringIncluyeDatos() {
        String texto = plato.toString();
        assertNotNull(texto);
        assertTrue(texto.startsWith("PlatoFuerte{"));
        assertTrue(texto.contains("codigo=P1"));
    }

    @Test
    @DisplayName("getCategoria de PlatoFuerte devuelve \"PlatoFuerte\"")
    void getCategoria() {
        assertEquals("PlatoFuerte", plato.getCategoria());
    }
}
