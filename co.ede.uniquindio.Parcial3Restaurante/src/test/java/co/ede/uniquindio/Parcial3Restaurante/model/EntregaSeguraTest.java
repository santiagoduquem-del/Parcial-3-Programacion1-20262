package co.ede.uniquindio.Parcial3Restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EntregaSeguraTest {

    private static final double DELTA = 0.001;
    private EntregaSegura empresa;

    @BeforeEach
    void setUp() {
        empresa = new EntregaSegura();
    }

    private Pedido pedidoDomicilio(double distancia) {
        Cliente cliente = new Cliente("1001", "Ana Gomez", "3001234567", "ana@correo.com", "Calle 1");
        Pedido pedido = new Pedido("PD1", LocalDate.of(2026, 11, 11), ModalidadEntrega.DOMICILIO, cliente);
        pedido.setDistanciaKm(distancia);
        return pedido;
    }

    // ---------- Pruebas base ----------

    @Test
    @DisplayName("EntregaSegura se crea con tarifa 7.000, 1.000 por km, maximo 20 km y recargo 3.000 desde 5 km")
    void datosDelConstructor() {
        assertEquals("EntregaSegura", empresa.getNombre());
        assertEquals(7000, empresa.getTarifaBase(), DELTA);
        assertEquals(1000, empresa.getTarifaKm(), DELTA);
        assertEquals(20, empresa.getDistanciaMaxima(), DELTA);
        assertEquals(3000, empresa.getRecargoDistancia(), DELTA);
        assertEquals(5, empresa.getUmbralRecargoKm(), DELTA);
    }

    @Test
    @DisplayName("Sin superar el umbral de 5 km no se cobra recargo")
    void costoSinRecargo() {
        assertEquals(12000, empresa.calcularCostoDomicilio(5), DELTA);
    }

    @Test
    @DisplayName("Al superar el umbral de 5 km se suma el recargo de 3.000")
    void costoConRecargo() {
        assertEquals(16000, empresa.calcularCostoDomicilio(6), DELTA);
    }

    @Test
    @DisplayName("Acepta un recorrido de 20 km")
    void aceptaDistanciaMaxima() {
        assertTrue(empresa.aceptaDistancia(20));
    }

    @Test
    @DisplayName("Rechaza un recorrido de 21 km")
    void rechazaDistanciaMayor() {
        assertFalse(empresa.aceptaDistancia(21));
    }

    // ---------- Pruebas ampliadas ----------

    @Test
    @DisplayName("solicitarServicio: EntregaSegura acepta un recorrido de 15 km")
    void solicitarServicioAcepta() {
        Pedido pedido = pedidoDomicilio(15);
        assertTrue(empresa.solicitarServicio(pedido));
        assertTrue(pedido.isDomicilioAceptado());
        assertSame(empresa, pedido.getTheEmpresaDomicilio());
        assertEquals(25000, pedido.calcularCostoDomicilio(), DELTA);
    }

    @Test
    @DisplayName("solicitarServicio: EntregaSegura rechaza un recorrido de 25 km")
    void solicitarServicioRechaza() {
        Pedido pedido = pedidoDomicilio(25);
        assertFalse(empresa.solicitarServicio(pedido));
        assertFalse(pedido.isDomicilioAceptado());
        assertNull(pedido.getTheEmpresaDomicilio());
    }

    @Test
    @DisplayName("Una empresa que rechaza le quita el domicilio aceptado previamente por otra")
    void rechazoReemplazaAceptacionAnterior() {
        Pedido pedido = pedidoDomicilio(3);
        assertTrue(new RapidoExpress().solicitarServicio(pedido));
        pedido.setDistanciaKm(30);
        assertFalse(empresa.solicitarServicio(pedido));
        assertFalse(pedido.isDomicilioAceptado());
        assertEquals(0, pedido.calcularCostoDomicilio(), DELTA);
    }

    @Test
    @DisplayName("Los setters de recargo y umbral cambian el calculo del costo")
    void settersRecargo() {
        empresa.setRecargoDistancia(5000);
        empresa.setUmbralRecargoKm(10);
        assertEquals(5000, empresa.getRecargoDistancia(), DELTA);
        assertEquals(10, empresa.getUmbralRecargoKm(), DELTA);
        assertEquals(15000, empresa.calcularCostoDomicilio(8), DELTA);
        assertEquals(23000, empresa.calcularCostoDomicilio(11), DELTA);
        assertTrue(empresa.toString().startsWith("EntregaSegura{"));
    }
}
