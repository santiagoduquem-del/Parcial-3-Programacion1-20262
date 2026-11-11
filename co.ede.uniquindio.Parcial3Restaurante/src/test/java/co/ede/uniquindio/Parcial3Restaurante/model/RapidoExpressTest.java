package co.ede.uniquindio.Parcial3Restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RapidoExpressTest {

    private static final double DELTA = 0.001;
    private RapidoExpress empresa;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        empresa = new RapidoExpress();
        Cliente cliente = new Cliente("1001", "Ana Gomez", "3001234567", "ana@correo.com", "Calle 1");
        pedido = new Pedido("PD1", LocalDate.of(2026, 11, 11), ModalidadEntrega.DOMICILIO, cliente);
    }

    // ---------- Pruebas base ----------

    @Test
    @DisplayName("RapidoExpress se crea con tarifa base 5.000, 1.500 por km y maximo 12 km")
    void datosDelConstructor() {
        assertEquals("RapidoExpress", empresa.getNombre());
        assertEquals(5000, empresa.getTarifaBase(), DELTA);
        assertEquals(1500, empresa.getTarifaKm(), DELTA);
        assertEquals(12, empresa.getDistanciaMaxima(), DELTA);
    }

    @Test
    @DisplayName("El costo del domicilio es tarifa base mas tarifa por km por la distancia")
    void calcularCosto() {
        assertEquals(9500, empresa.calcularCostoDomicilio(3), DELTA);
    }

    @Test
    @DisplayName("Acepta un recorrido igual a la distancia maxima")
    void aceptaDistanciaMaxima() {
        assertTrue(empresa.aceptaDistancia(12));
    }

    @Test
    @DisplayName("Rechaza un recorrido mayor que la distancia maxima")
    void rechazaDistanciaMayor() {
        assertFalse(empresa.aceptaDistancia(12.5));
    }

    @Test
    @DisplayName("Rechaza una distancia de cero kilometros")
    void rechazaDistanciaCero() {
        assertFalse(empresa.aceptaDistancia(0));
    }

    // ---------- Pruebas ampliadas: solicitarServicio ----------

    @Test
    @DisplayName("solicitarServicio: la empresa acepta y queda asignada al pedido")
    void solicitarServicioAcepta() {
        pedido.setDistanciaKm(5);
        assertTrue(empresa.solicitarServicio(pedido));
        assertTrue(pedido.isDomicilioAceptado());
        assertSame(empresa, pedido.getTheEmpresaDomicilio());
    }

    @Test
    @DisplayName("solicitarServicio: la empresa rechaza una distancia mayor y el pedido queda sin empresa")
    void solicitarServicioRechaza() {
        pedido.setDistanciaKm(15);
        assertFalse(empresa.solicitarServicio(pedido));
        assertFalse(pedido.isDomicilioAceptado());
        assertNull(pedido.getTheEmpresaDomicilio());
    }

    @Test
    @DisplayName("solicitarServicio: rechaza un pedido nulo o que no es a domicilio")
    void solicitarServicioPedidoNoDomicilio() {
        assertFalse(empresa.solicitarServicio(null));
        pedido.setModalidadEntrega(ModalidadEntrega.CONSUMO_RESTAURANTE);
        pedido.setDistanciaKm(3);
        assertFalse(empresa.solicitarServicio(pedido));
        assertFalse(pedido.isDomicilioAceptado());
    }

    @Test
    @DisplayName("Los setters de EmpresaDomicilio cambian el costo y la distancia maxima")
    void settersEmpresa() {
        empresa.setNombre("Rapido 2");
        empresa.setTarifaBase(1000);
        empresa.setTarifaKm(100);
        empresa.setDistanciaMaxima(20);
        assertEquals("Rapido 2", empresa.getNombre());
        assertEquals(1500, empresa.calcularCostoDomicilio(5), DELTA);
        assertTrue(empresa.aceptaDistancia(20));
        assertTrue(empresa.toString().startsWith("RapidoExpress{"));
    }
}
