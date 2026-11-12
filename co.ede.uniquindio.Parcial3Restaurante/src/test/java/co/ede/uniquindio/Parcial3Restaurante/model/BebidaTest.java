package co.ede.uniquindio.Parcial3Restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BebidaTest {

    private static final double DELTA = 0.001;
    private Bebida fria;
    private Bebida caliente;

    @BeforeEach
    void setUp() {
        fria = new Bebida("B1", "Limonada", "Natural", 5000, 5, 350, 4, true, false);
        caliente = new Bebida("B2", "Cafe", "Tinto", 3000, 3, 200, 70, false, true);
    }

    // ---------- Pruebas base ----------

    @Test
    @DisplayName("El precio de venta sin preparacion especial es el precio base mas el 10 %")
    void precioSinPreparacionEspecial() {
        assertEquals(5500, fria.calcularPrecioVenta(), DELTA);
    }

    @Test
    @DisplayName("La preparacion especial suma $3.000 fijos al precio de venta")
    void precioConPreparacionEspecial() {
        assertEquals(3300 + 3000, caliente.calcularPrecioVenta(), DELTA);
    }

    @Test
    @DisplayName("Una bebida fria entre 0 y 10 grados y volumen valido cumple calidad")
    void friaCumpleCalidad() {
        assertTrue(fria.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Una bebida fria a mas de 10 grados no cumple calidad")
    void friaTemperaturaAlta() {
        fria.setTemperaturaServicio(15);
        assertFalse(fria.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Una bebida caliente entre 55 y 85 grados cumple calidad")
    void calienteCumpleCalidad() {
        assertTrue(caliente.cumpleCondicionesCalidad());
    }

    // ---------- Pruebas ampliadas ----------

    @Test
    @DisplayName("Una bebida con volumen fuera de 150-1500 ml no cumple calidad")
    void volumenFueraDeRango() {
        fria.setVolumenMl(100);
        assertFalse(fria.cumpleCondicionesCalidad());
        fria.setVolumenMl(1600);
        assertFalse(fria.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Los limites de volumen y temperatura son inclusivos")
    void limitesInclusivos() {
        fria.setVolumenMl(150);
        fria.setTemperaturaServicio(0);
        assertTrue(fria.cumpleCondicionesCalidad());
        caliente.setVolumenMl(1500);
        caliente.setTemperaturaServicio(85);
        assertTrue(caliente.cumpleCondicionesCalidad());
        caliente.setTemperaturaServicio(54.9);
        assertFalse(caliente.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Cambiar fria/caliente con el setter cambia el rango de temperatura exigido")
    void cambiarFriaCambiaRango() {
        fria.setFria(false);
        assertFalse(fria.isFria());
        assertFalse(fria.cumpleCondicionesCalidad());
        fria.setTemperaturaServicio(60);
        assertEquals(60, fria.getTemperaturaServicio(), DELTA);
        assertTrue(fria.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Los getters y setters de Bebida guardan los valores")
    void gettersYSetters() {
        fria.setRequierePreparacionEspecial(true);
        assertTrue(fria.isRequierePreparacionEspecial());
        assertEquals(350, fria.getVolumenMl(), DELTA);
        assertEquals(8500, fria.calcularPrecioVenta(), DELTA);
        assertTrue(fria.toString().startsWith("Bebida{"));
    }

    @Test
    @DisplayName("getCategoria de Bebida devuelve \"Bebida\"")
    void getCategoria() {
        assertEquals("Bebida", fria.getCategoria());
    }
}
