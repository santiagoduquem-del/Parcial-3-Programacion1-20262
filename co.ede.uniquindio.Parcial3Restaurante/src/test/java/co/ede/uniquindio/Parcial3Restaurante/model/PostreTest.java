package co.ede.uniquindio.Parcial3Restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostreTest {

    private static final double DELTA = 0.001;
    private Postre refrigerado;
    private Postre ambiente;

    @BeforeEach
    void setUp() {
        refrigerado = new Postre("D1", "Flan", "De caramelo", 8000, 10, 150, 4, true);
        ambiente = new Postre("D2", "Brownie", "De chocolate", 6000, 20, 120, 22, false);
    }

    // ---------- Pruebas base ----------

    @Test
    @DisplayName("El precio de un postre sin refrigeracion es el precio base mas el 15 %")
    void precioSinRefrigeracion() {
        assertEquals(6900, ambiente.calcularPrecioVenta(), DELTA);
    }

    @Test
    @DisplayName("La refrigeracion agrega un 5 % adicional sobre el precio base")
    void precioConRefrigeracion() {
        assertEquals(9600, refrigerado.calcularPrecioVenta(), DELTA);
    }

    @Test
    @DisplayName("Un postre refrigerado entre 0 y 6 grados cumple calidad")
    void refrigeradoCumpleCalidad() {
        assertTrue(refrigerado.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Un postre refrigerado a mas de 6 grados no cumple calidad")
    void refrigeradoTemperaturaAlta() {
        refrigerado.setTemperaturaAlmacenamiento(8);
        assertFalse(refrigerado.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Un postre sin refrigeracion entre 15 y 30 grados cumple calidad")
    void ambienteCumpleCalidad() {
        assertTrue(ambiente.cumpleCondicionesCalidad());
    }

    // ---------- Pruebas ampliadas ----------

    @Test
    @DisplayName("Un postre con peso fuera de 80-500 g no cumple calidad")
    void pesoFueraDeRango() {
        ambiente.setPesoGramos(79);
        assertFalse(ambiente.cumpleCondicionesCalidad());
        ambiente.setPesoGramos(501);
        assertFalse(ambiente.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Los limites de peso y temperatura son inclusivos")
    void limitesInclusivos() {
        refrigerado.setPesoGramos(80);
        refrigerado.setTemperaturaAlmacenamiento(0);
        assertTrue(refrigerado.cumpleCondicionesCalidad());
        ambiente.setPesoGramos(500);
        ambiente.setTemperaturaAlmacenamiento(30);
        assertTrue(ambiente.cumpleCondicionesCalidad());
        ambiente.setTemperaturaAlmacenamiento(14);
        assertFalse(ambiente.cumpleCondicionesCalidad());
    }

    @Test
    @DisplayName("Los getters y setters de Postre guardan los valores y afectan el precio")
    void gettersYSetters() {
        ambiente.setRequiereRefrigeracion(true);
        assertTrue(ambiente.isRequiereRefrigeracion());
        assertEquals(120, ambiente.getPesoGramos(), DELTA);
        assertEquals(22, ambiente.getTemperaturaAlmacenamiento(), DELTA);
        assertEquals(7200, ambiente.calcularPrecioVenta(), DELTA);
        assertFalse(ambiente.cumpleCondicionesCalidad());
        assertTrue(ambiente.toString().startsWith("Postre{"));
    }

    @Test
    @DisplayName("getCategoria de Postre devuelve \"Postre\"")
    void getCategoria() {
        assertEquals("Postre", refrigerado.getCategoria());
    }
}
