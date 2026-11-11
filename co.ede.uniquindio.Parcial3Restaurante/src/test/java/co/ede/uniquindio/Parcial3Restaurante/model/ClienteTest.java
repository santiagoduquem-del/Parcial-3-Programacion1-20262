package co.ede.uniquindio.Parcial3Restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Cliente cliente;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("1001", "Ana Gomez", "3001234567", "ana@correo.com", "Calle 1");
        pedido = new Pedido("PE1", LocalDate.of(2026, 11, 11), ModalidadEntrega.CONSUMO_RESTAURANTE, cliente);
    }

    // ---------- Pruebas base ----------

    @Test
    @DisplayName("El constructor guarda los datos y crea el historial vacio")
    void constructor() {
        assertEquals("1001", cliente.getDocumento());
        assertEquals("Ana Gomez", cliente.getNombreCompleto());
        assertEquals("3001234567", cliente.getTelefono());
        assertEquals("ana@correo.com", cliente.getCorreo());
        assertEquals("Calle 1", cliente.getDireccionResidencia());
        assertNotNull(cliente.getListPedidos());
        assertTrue(cliente.getListPedidos().isEmpty());
    }

    @Test
    @DisplayName("agregarPedido agrega un pedido nuevo al historial")
    void agregarPedido() {
        assertTrue(cliente.agregarPedido(pedido));
        assertEquals(1, cliente.getListPedidos().size());
    }

    @Test
    @DisplayName("agregarPedido no repite un pedido con el mismo codigo")
    void agregarPedidoDuplicado() {
        cliente.agregarPedido(pedido);
        Pedido mismoCodigo = new Pedido("PE1", LocalDate.now(), ModalidadEntrega.DOMICILIO, cliente);
        assertFalse(cliente.agregarPedido(mismoCodigo));
        assertEquals(1, cliente.getListPedidos().size());
    }

    @Test
    @DisplayName("agregarPedido rechaza un pedido nulo")
    void agregarPedidoNulo() {
        assertFalse(cliente.agregarPedido(null));
        assertTrue(cliente.getListPedidos().isEmpty());
    }

    @Test
    @DisplayName("buscarPedido devuelve null si el cliente no tiene ese pedido")
    void buscarPedidoInexistente() {
        assertNull(cliente.buscarPedido("NO-EXISTE"));
    }

    // ---------- Pruebas ampliadas ----------

    @Test
    @DisplayName("buscarPedido devuelve el pedido del historial por su codigo")
    void buscarPedidoExistente() {
        cliente.agregarPedido(pedido);
        assertSame(pedido, cliente.buscarPedido("PE1"));
    }

    @Test
    @DisplayName("Los setters permiten actualizar los datos del cliente")
    void actualizarDatos() {
        cliente.setDocumento("2002");
        cliente.setNombreCompleto("Ana Maria Gomez");
        cliente.setTelefono("3109876543");
        cliente.setCorreo("anamaria@correo.com");
        cliente.setDireccionResidencia("Carrera 5");
        assertEquals("2002", cliente.getDocumento());
        assertEquals("Ana Maria Gomez", cliente.getNombreCompleto());
        assertEquals("3109876543", cliente.getTelefono());
        assertEquals("anamaria@correo.com", cliente.getCorreo());
        assertEquals("Carrera 5", cliente.getDireccionResidencia());
    }

    @Test
    @DisplayName("setListPedidos reemplaza el historial y toString muestra el total")
    void setListPedidosYToString() {
        List<Pedido> lista = new ArrayList<>();
        lista.add(pedido);
        cliente.setListPedidos(lista);
        assertSame(lista, cliente.getListPedidos());
        assertTrue(cliente.toString().contains("totalPedidos=1"));
    }
}
