package co.ede.uniquindio.Parcial3Restaurante;

import co.ede.uniquindio.Parcial3Restaurante.model.*;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * Menu interactivo (JOptionPane) de El Buen Comer.
 * <p>
 * Toda la logica de negocio vive en {@link Restaurante}; esta clase solo
 * pide datos, llama al restaurante y muestra los resultados.
 * <p>
 * CRUD: productos y clientes (crear, consultar, actualizar, eliminar).
 * Pedidos: crear, consultar, actualizar (agregar productos, domicilio,
 * confirmar, entregar) y cancelar.
 */
public class Main {

    private static final String TITULO = "El Buen Comer";
    private static final NumberFormat FORMATO_DINERO =
            NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CO"));

    private static final Restaurante restaurante =
            new Restaurante("El Buen Comer", "900123456-7", "Armenia, Quindio");

    /** Operacion del menu que puede fallar por una regla de negocio. */
    @FunctionalInterface
    private interface Accion {
        void ejecutar() throws RestauranteException;
    }

    /** Se lanza cuando el usuario cierra o cancela un dialogo para abortar la operacion. */
    private static class OperacionCancelada extends RuntimeException {
        OperacionCancelada(String mensaje) {
            super(mensaje);
        }
    }

    /** Datos comunes a los tres tipos de producto. */
    private static class DatosBase {
        String codigo;
        String nombre;
        String descripcion;
        double precioBase;
        int tiempo;
    }

    public static void main(String[] args) {
        menuPrincipal();
    }

    // =====================================================================
    // MENUS
    // =====================================================================

    private static void menuPrincipal() {
        String[] opciones = {
                "1. Gestion de productos",
                "2. Gestion de clientes",
                "3. Gestion de pedidos",
                "4. Consultas y reportes",
                "0. Salir"
        };
        String encabezado = restaurante.getNombreComercial() + " - NIT " + restaurante.getNit()
                + "\n" + restaurante.getDireccion() + "\n\nSeleccione una opcion:";
        boolean salir = false;
        while (!salir) {
            String eleccion = elegirOpcion("Menu principal", encabezado, opciones);
            if (eleccion == null || eleccion.startsWith("0")) {
                salir = preguntar("Desea salir de la aplicacion?", false) == 0;
            } else {
                switch (eleccion.charAt(0)) {
                    case '1':
                        menuProductos();
                        break;
                    case '2':
                        menuClientes();
                        break;
                    case '3':
                        menuPedidos();
                        break;
                    case '4':
                        menuConsultas();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static void menuProductos() {
        String[] opciones = {
                "1. Crear plato fuerte",
                "2. Crear bebida",
                "3. Crear postre",
                "4. Listar productos",
                "5. Buscar producto por codigo",
                "6. Actualizar producto",
                "7. Eliminar producto",
                "0. Volver"
        };
        String eleccion = elegirOpcion("Gestion de productos", "Seleccione una opcion:", opciones);
        while (eleccion != null && !eleccion.startsWith("0")) {
            switch (eleccion.charAt(0)) {
                case '1':
                    ejecutar(Main::crearPlatoFuerte);
                    break;
                case '2':
                    ejecutar(Main::crearBebida);
                    break;
                case '3':
                    ejecutar(Main::crearPostre);
                    break;
                case '4':
                    ejecutar(Main::listarProductos);
                    break;
                case '5':
                    ejecutar(Main::buscarProducto);
                    break;
                case '6':
                    ejecutar(Main::actualizarProducto);
                    break;
                case '7':
                    ejecutar(Main::eliminarProducto);
                    break;
                default:
                    break;
            }
            eleccion = elegirOpcion("Gestion de productos", "Seleccione una opcion:", opciones);
        }
    }

    private static void menuClientes() {
        String[] opciones = {
                "1. Registrar cliente",
                "2. Listar clientes",
                "3. Buscar cliente por documento",
                "4. Actualizar cliente",
                "5. Eliminar cliente",
                "0. Volver"
        };
        String eleccion = elegirOpcion("Gestion de clientes", "Seleccione una opcion:", opciones);
        while (eleccion != null && !eleccion.startsWith("0")) {
            switch (eleccion.charAt(0)) {
                case '1':
                    ejecutar(Main::crearCliente);
                    break;
                case '2':
                    ejecutar(Main::listarClientes);
                    break;
                case '3':
                    ejecutar(Main::buscarCliente);
                    break;
                case '4':
                    ejecutar(Main::actualizarCliente);
                    break;
                case '5':
                    ejecutar(Main::eliminarCliente);
                    break;
                default:
                    break;
            }
            eleccion = elegirOpcion("Gestion de clientes", "Seleccione una opcion:", opciones);
        }
    }

    private static void menuPedidos() {
        String[] opciones = {
                "1. Crear pedido",
                "2. Agregar producto a un pedido",
                "3. Solicitar domicilio",
                "4. Confirmar pedido",
                "5. Marcar pedido como entregado",
                "6. Cancelar pedido",
                "7. Consultar pedido (detalle y valor total)",
                "8. Listar pedidos",
                "0. Volver"
        };
        String eleccion = elegirOpcion("Gestion de pedidos", "Seleccione una opcion:", opciones);
        while (eleccion != null && !eleccion.startsWith("0")) {
            switch (eleccion.charAt(0)) {
                case '1':
                    ejecutar(Main::crearPedido);
                    break;
                case '2':
                    ejecutar(Main::agregarProductoAPedido);
                    break;
                case '3':
                    ejecutar(Main::solicitarDomicilioAPedido);
                    break;
                case '4':
                    ejecutar(Main::confirmarPedido);
                    break;
                case '5':
                    ejecutar(Main::entregarPedido);
                    break;
                case '6':
                    ejecutar(Main::cancelarPedido);
                    break;
                case '7':
                    ejecutar(Main::consultarPedido);
                    break;
                case '8':
                    ejecutar(Main::listarPedidos);
                    break;
                default:
                    break;
            }
            eleccion = elegirOpcion("Gestion de pedidos", "Seleccione una opcion:", opciones);
        }
    }

    private static void menuConsultas() {
        String[] opciones = {
                "1. Productos disponibles (cumplen calidad)",
                "2. Productos no disponibles (no cumplen calidad)",
                "3. Historial de pedidos de un cliente (por documento)",
                "4. Ingresos del restaurante y de domicilios",
                "5. Producto mas vendido",
                "6. Categoria con mayores ingresos",
                "7. Empresas de domicilio",
                "0. Volver"
        };
        String eleccion = elegirOpcion("Consultas y reportes", "Seleccione una opcion:", opciones);
        while (eleccion != null && !eleccion.startsWith("0")) {
            switch (eleccion.charAt(0)) {
                case '1':
                    ejecutar(Main::consultarProductosDisponibles);
                    break;
                case '2':
                    ejecutar(Main::consultarProductosNoDisponibles);
                    break;
                case '3':
                    ejecutar(Main::consultarHistorialCliente);
                    break;
                case '4':
                    ejecutar(Main::consultarIngresos);
                    break;
                case '5':
                    ejecutar(Main::consultarProductoMasVendido);
                    break;
                case '6':
                    ejecutar(Main::consultarCategoriaMayoresIngresos);
                    break;
                case '7':
                    ejecutar(Main::consultarEmpresasDomicilio);
                    break;
                default:
                    break;
            }
            eleccion = elegirOpcion("Consultas y reportes", "Seleccione una opcion:", opciones);
        }
    }

    /**
     * Ejecuta una operacion del menu mostrando como mensaje cualquier regla
     * de negocio incumplida, en vez de cerrar el programa.
     */
    private static void ejecutar(Accion accion) {
        try {
            accion.ejecutar();
        } catch (OperacionCancelada e) {
            if (e.getMessage() != null) {
                informar(e.getMessage());
            }
        } catch (RestauranteException e) {
            error(e.getMessage());
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        }
    }

    // =====================================================================
    // PRODUCTOS (CRUD)
    // =====================================================================

    private static void crearPlatoFuerte() throws RestauranteException {
        DatosBase d = leerDatosBase(null);
        double peso = leerDecimalPositivo("Peso (gramos):", "");
        TipoPreparacion tipo = elegirEnum("Tipo de preparacion:", TipoPreparacion.values(), null);
        boolean coccion = leerSiNo("La coccion fue aprobada?", true);
        Producto nuevo = new PlatoFuerte(d.codigo, d.nombre, d.descripcion, d.precioBase, d.tiempo,
                peso, tipo, coccion);
        restaurante.registrarProducto(nuevo);
        mostrarTexto("Plato fuerte registrado", describirProducto(nuevo));
    }

    private static void crearBebida() throws RestauranteException {
        DatosBase d = leerDatosBase(null);
        double volumen = leerDecimalPositivo("Volumen (mililitros):", "");
        boolean fria = leerSiNo("La bebida se sirve fria?", true);
        double temperatura = leerDecimal("Temperatura de servicio (grados C):", "");
        boolean especial = leerSiNo("Requiere preparacion especial?", false);
        Producto nuevo = new Bebida(d.codigo, d.nombre, d.descripcion, d.precioBase, d.tiempo,
                volumen, temperatura, fria, especial);
        restaurante.registrarProducto(nuevo);
        mostrarTexto("Bebida registrada", describirProducto(nuevo));
    }

    private static void crearPostre() throws RestauranteException {
        DatosBase d = leerDatosBase(null);
        double peso = leerDecimalPositivo("Peso (gramos):", "");
        boolean refrigeracion = leerSiNo("Requiere refrigeracion?", true);
        double temperatura = leerDecimal("Temperatura de almacenamiento (grados C):", "");
        Producto nuevo = new Postre(d.codigo, d.nombre, d.descripcion, d.precioBase, d.tiempo,
                peso, temperatura, refrigeracion);
        restaurante.registrarProducto(nuevo);
        mostrarTexto("Postre registrado", describirProducto(nuevo));
    }

    private static void listarProductos() {
        List<Producto> productos = restaurante.getListProductos();
        if (productos.isEmpty()) {
            throw new OperacionCancelada("No hay productos registrados.");
        }
        mostrarTexto("Menu de productos (" + productos.size() + ")", describirProductos(productos));
    }

    private static void buscarProducto() throws RestauranteException {
        String codigo = leerTexto("Codigo del producto:", "");
        Producto producto = restaurante.buscarProducto(codigo);
        if (producto == null) {
            throw new ElementoNoEncontradoException("Producto", codigo);
        }
        mostrarTexto("Producto " + codigo, describirProducto(producto));
    }

    private static void actualizarProducto() throws RestauranteException {
        Producto p = elegirProducto("Seleccione el producto a actualizar:");
        // Primero se leen todos los datos nuevos; solo si el usuario no cancela se aplican.
        DatosBase d = leerDatosBase(p);
        if (p instanceof PlatoFuerte) {
            PlatoFuerte pf = (PlatoFuerte) p;
            double peso = leerDecimalPositivo("Peso (gramos):", numero(pf.getPesoGramos()));
            TipoPreparacion tipo = elegirEnum("Tipo de preparacion:", TipoPreparacion.values(),
                    pf.getTipoPreparacion());
            boolean coccion = leerSiNo("La coccion fue aprobada?", pf.isCoccionAprobada());
            aplicarDatosBase(p, d);
            pf.setPesoGramos(peso);
            pf.setTipoPreparacion(tipo);
            pf.setCoccionAprobada(coccion);
        } else if (p instanceof Bebida) {
            Bebida b = (Bebida) p;
            double volumen = leerDecimalPositivo("Volumen (mililitros):", numero(b.getVolumenMl()));
            boolean fria = leerSiNo("La bebida se sirve fria?", b.isFria());
            double temperatura = leerDecimal("Temperatura de servicio (grados C):",
                    numero(b.getTemperaturaServicio()));
            boolean especial = leerSiNo("Requiere preparacion especial?", b.isRequierePreparacionEspecial());
            aplicarDatosBase(p, d);
            b.setVolumenMl(volumen);
            b.setFria(fria);
            b.setTemperaturaServicio(temperatura);
            b.setRequierePreparacionEspecial(especial);
        } else if (p instanceof Postre) {
            Postre po = (Postre) p;
            double peso = leerDecimalPositivo("Peso (gramos):", numero(po.getPesoGramos()));
            boolean refrigeracion = leerSiNo("Requiere refrigeracion?", po.isRequiereRefrigeracion());
            double temperatura = leerDecimal("Temperatura de almacenamiento (grados C):",
                    numero(po.getTemperaturaAlmacenamiento()));
            aplicarDatosBase(p, d);
            po.setPesoGramos(peso);
            po.setRequiereRefrigeracion(refrigeracion);
            po.setTemperaturaAlmacenamiento(temperatura);
        }
        mostrarTexto("Producto actualizado", describirProducto(p));
    }

    private static void eliminarProducto() throws RestauranteException {
        Producto p = elegirProducto("Seleccione el producto a eliminar:");
        if (preguntar("Eliminar el producto " + p.getCodigo() + " - " + p.getNombre() + "?", false) == 0) {
            restaurante.eliminarProducto(p.getCodigo());
            informar("Producto eliminado.");
        }
    }

    /**
     * Pide los datos comunes de un producto. Si {@code actual} es null se
     * esta creando (se pide el codigo y se verifica que no exista); si no,
     * se esta actualizando (el codigo no cambia y los campos salen rellenos).
     */
    private static DatosBase leerDatosBase(Producto actual) throws ElementoDuplicadoException {
        DatosBase d = new DatosBase();
        if (actual == null) {
            d.codigo = leerTexto("Codigo del producto:", "");
            if (restaurante.buscarProducto(d.codigo) != null) {
                throw new ElementoDuplicadoException("Producto", d.codigo);
            }
        } else {
            d.codigo = actual.getCodigo();
        }
        d.nombre = leerTexto("Nombre:", actual == null ? "" : actual.getNombre());
        d.descripcion = leerTexto("Descripcion:", actual == null ? "" : actual.getDescripcion());
        d.precioBase = leerDecimalPositivo("Precio base (en pesos):",
                actual == null ? "" : numero(actual.getPrecioBase()));
        d.tiempo = leerEnteroPositivo("Tiempo de preparacion (minutos):",
                actual == null ? "" : String.valueOf(actual.getTiempoPreparacion()));
        return d;
    }

    private static void aplicarDatosBase(Producto p, DatosBase d) {
        p.setNombre(d.nombre);
        p.setDescripcion(d.descripcion);
        p.setPrecioBase(d.precioBase);
        p.setTiempoPreparacion(d.tiempo);
    }

    private static Producto elegirProducto(String mensaje) {
        return elegir(mensaje, restaurante.getListProductos(),
                p -> p.getCodigo() + " - " + p.getNombre(),
                "No hay productos registrados.", null);
    }

    // =====================================================================
    // CLIENTES (CRUD)
    // =====================================================================

    private static void crearCliente() throws RestauranteException {
        String documento = leerTexto("Numero de documento:", "");
        if (restaurante.buscarCliente(documento) != null) {
            throw new ElementoDuplicadoException("Cliente", documento);
        }
        String nombre = leerTexto("Nombre completo:", "");
        String telefono = leerTexto("Telefono:", "");
        String correo = leerCorreo("");
        String direccion = leerTexto("Direccion de residencia:", "");
        Cliente nuevo = new Cliente(documento, nombre, telefono, correo, direccion);
        restaurante.registrarCliente(nuevo);
        mostrarTexto("Cliente registrado", describirCliente(nuevo));
    }

    private static void listarClientes() {
        List<Cliente> clientes = restaurante.getListClientes();
        if (clientes.isEmpty()) {
            throw new OperacionCancelada("No hay clientes registrados.");
        }
        StringBuilder sb = new StringBuilder();
        for (Cliente c : clientes) {
            sb.append(describirCliente(c)).append("\n\n");
        }
        mostrarTexto("Clientes (" + clientes.size() + ")", sb.toString().trim());
    }

    private static void buscarCliente() throws RestauranteException {
        String documento = leerTexto("Numero de documento:", "");
        Cliente cliente = restaurante.buscarCliente(documento);
        if (cliente == null) {
            throw new ElementoNoEncontradoException("Cliente", documento);
        }
        mostrarTexto("Cliente " + documento, describirCliente(cliente));
    }

    private static void actualizarCliente() throws RestauranteException {
        Cliente c = elegirCliente("Seleccione el cliente a actualizar:");
        // El documento identifica al cliente y no se modifica.
        String nombre = leerTexto("Nombre completo:", c.getNombreCompleto());
        String telefono = leerTexto("Telefono:", c.getTelefono());
        String correo = leerCorreo(c.getCorreo());
        String direccion = leerTexto("Direccion de residencia:", c.getDireccionResidencia());
        c.setNombreCompleto(nombre);
        c.setTelefono(telefono);
        c.setCorreo(correo);
        c.setDireccionResidencia(direccion);
        mostrarTexto("Cliente actualizado", describirCliente(c));
    }

    private static void eliminarCliente() throws RestauranteException {
        Cliente c = elegirCliente("Seleccione el cliente a eliminar:");
        if (preguntar("Eliminar al cliente " + c.getDocumento() + " - " + c.getNombreCompleto() + "?", false) == 0) {
            restaurante.eliminarCliente(c.getDocumento());
            informar("Cliente eliminado.");
        }
    }

    private static Cliente elegirCliente(String mensaje) {
        return elegir(mensaje, restaurante.getListClientes(),
                c -> c.getDocumento() + " - " + c.getNombreCompleto(),
                "No hay clientes registrados. Registre un cliente primero.", null);
    }

    private static String leerCorreo(String inicial) {
        String intento = inicial;
        while (true) {
            intento = leerTexto("Correo electronico:", intento);
            if (intento.contains("@") && intento.indexOf('@') > 0 && intento.indexOf('@') < intento.length() - 1) {
                return intento;
            }
            error("Ingrese un correo valido (ejemplo: nombre@dominio.com).");
        }
    }

    // =====================================================================
    // PEDIDOS
    // =====================================================================

    private static void crearPedido() throws RestauranteException {
        Cliente cliente = elegirCliente("Cliente que realiza el pedido:");
        String codigo = leerTexto("Codigo del pedido:", "");
        if (restaurante.buscarPedido(codigo) != null) {
            throw new ElementoDuplicadoException("Pedido", codigo);
        }
        LocalDate fecha = leerFecha("Fecha del pedido (AAAA-MM-DD):", LocalDate.now().toString());
        ModalidadEntrega modalidad = elegirEnum("Modalidad de entrega:", ModalidadEntrega.values(), null);

        Pedido pedido = new Pedido(codigo, fecha, modalidad, cliente);
        restaurante.registrarPedido(pedido);
        informar("Pedido '" + codigo + "' creado en estado PENDIENTE.");

        if (modalidad == ModalidadEntrega.DOMICILIO
                && preguntar("Desea solicitar el domicilio ahora?", true) == 0) {
            ejecutar(() -> solicitarDomicilio(pedido));
        }
        while (preguntar("Desea agregar un producto al pedido?", true) == 0) {
            ejecutar(() -> agregarProducto(pedido));
        }
    }

    private static void agregarProductoAPedido() throws RestauranteException {
        Pedido pedido = elegirPedido("Pedido al que desea agregar productos:",
                pedidosConEstado(EstadoPedido.PENDIENTE), "No hay pedidos pendientes.");
        agregarProducto(pedido);
    }

    private static void agregarProducto(Pedido pedido) throws RestauranteException {
        Producto producto = elegirProducto("Producto a agregar al pedido " + pedido.getCodigo() + ":");
        int cantidad = leerEntero("Cantidad:", "1");
        restaurante.agregarProductoAPedido(pedido.getCodigo(), producto.getCodigo(), cantidad);
        informar("Producto agregado al pedido " + pedido.getCodigo() + ".\n"
                + "Valor actual de los productos: " + dinero(pedido.calcularValorProductos()));
    }

    private static void solicitarDomicilioAPedido() throws RestauranteException {
        List<Pedido> candidatos = new ArrayList<>();
        for (Pedido p : pedidosConEstado(EstadoPedido.PENDIENTE)) {
            if (p.getModalidadEntrega() == ModalidadEntrega.DOMICILIO) {
                candidatos.add(p);
            }
        }
        Pedido pedido = elegirPedido("Pedido a domicilio:", candidatos,
                "No hay pedidos pendientes a domicilio.");
        solicitarDomicilio(pedido);
    }

    private static void solicitarDomicilio(Pedido pedido) throws RestauranteException {
        EmpresaDomicilio empresa = elegir("Empresa de domicilio para el pedido " + pedido.getCodigo() + ":",
                restaurante.getListEmpresasDomicilio(),
                e -> e.getNombre() + " (hasta " + numero(e.getDistanciaMaxima()) + " km)",
                "No hay empresas de domicilio registradas.", null);
        double distancia = leerDecimalPositivo("Distancia del recorrido (km):", "");
        restaurante.solicitarDomicilio(pedido.getCodigo(), empresa.getNombre(), distancia);
        informar("La empresa " + empresa.getNombre() + " acepto el recorrido de " + numero(distancia) + " km.\n"
                + "Costo del domicilio: " + dinero(pedido.calcularCostoDomicilio()));
    }

    private static void confirmarPedido() throws RestauranteException {
        Pedido pedido = elegirPedido("Pedido a confirmar:",
                pedidosConEstado(EstadoPedido.PENDIENTE), "No hay pedidos pendientes.");
        restaurante.confirmarPedido(pedido.getCodigo());
        informar("Pedido " + pedido.getCodigo() + " confirmado.\n"
                + "Valor total: " + dinero(pedido.calcularTotal()));
    }

    private static void entregarPedido() throws RestauranteException {
        Pedido pedido = elegirPedido("Pedido a marcar como entregado:",
                pedidosConEstado(EstadoPedido.CONFIRMADO), "No hay pedidos confirmados.");
        restaurante.entregarPedido(pedido.getCodigo());
        informar("Pedido " + pedido.getCodigo() + " marcado como ENTREGADO.");
    }

    private static void cancelarPedido() throws RestauranteException {
        Pedido pedido = elegirPedido("Pedido a cancelar:",
                pedidosConEstado(EstadoPedido.PENDIENTE, EstadoPedido.CONFIRMADO),
                "No hay pedidos pendientes ni confirmados.");
        if (preguntar("Cancelar el pedido " + pedido.getCodigo() + "?", false) == 0) {
            restaurante.cancelarPedido(pedido.getCodigo());
            informar("Pedido " + pedido.getCodigo() + " cancelado.");
        }
    }

    private static void consultarPedido() throws RestauranteException {
        Pedido pedido = elegirPedido("Pedido a consultar:", restaurante.getListPedidos(),
                "No hay pedidos registrados.");
        double total = restaurante.calcularValorTotalPedido(pedido.getCodigo());
        mostrarTexto("Pedido " + pedido.getCodigo(), describirPedido(pedido, total));
    }

    private static void listarPedidos() {
        List<Pedido> pedidos = restaurante.getListPedidos();
        if (pedidos.isEmpty()) {
            throw new OperacionCancelada("No hay pedidos registrados.");
        }
        mostrarTexto("Pedidos (" + pedidos.size() + ")", describirPedidos(pedidos));
    }

    private static List<Pedido> pedidosConEstado(EstadoPedido... estados) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : restaurante.getListPedidos()) {
            for (EstadoPedido estado : estados) {
                if (pedido.getEstado() == estado) {
                    resultado.add(pedido);
                    break;
                }
            }
        }
        return resultado;
    }

    private static Pedido elegirPedido(String mensaje, List<Pedido> candidatos, String mensajeVacio) {
        return elegir(mensaje, candidatos,
                p -> p.getCodigo() + " - " + p.getTheCliente().getNombreCompleto() + " - " + p.getEstado(),
                mensajeVacio, null);
    }

    // =====================================================================
    // CONSULTAS Y REPORTES
    // =====================================================================

    private static void consultarProductosDisponibles() {
        List<Producto> lista = restaurante.consultarProductosDisponibles();
        if (lista.isEmpty()) {
            throw new OperacionCancelada("No hay productos que cumplan las condiciones de calidad.");
        }
        mostrarTexto("Productos disponibles (" + lista.size() + ")", describirProductos(lista));
    }

    private static void consultarProductosNoDisponibles() {
        List<Producto> lista = restaurante.consultarProductosNoDisponibles();
        if (lista.isEmpty()) {
            throw new OperacionCancelada("Todos los productos cumplen las condiciones de calidad.");
        }
        mostrarTexto("Productos no disponibles (" + lista.size() + ")", describirProductos(lista));
    }

    private static void consultarHistorialCliente() throws RestauranteException {
        String documento = leerTexto("Documento del cliente:", "");
        Cliente cliente = restaurante.buscarCliente(documento);
        if (cliente == null) {
            throw new ElementoNoEncontradoException("Cliente", documento);
        }
        List<Pedido> historial = restaurante.consultarHistorialCliente(documento);
        if (historial.isEmpty()) {
            throw new OperacionCancelada(cliente.getNombreCompleto() + " no tiene pedidos confirmados.");
        }
        mostrarTexto("Historial de " + cliente.getNombreCompleto() + " (" + historial.size() + ")",
                "Cliente: " + cliente.getDocumento() + " - " + cliente.getNombreCompleto() + "\n\n"
                        + describirHistorial(historial));
    }

    private static void consultarIngresos() {
        double restauranteIngresos = restaurante.calcularIngresosRestaurante();
        double domicilios = restaurante.calcularIngresosDomicilios();
        mostrarTexto("Ingresos",
                "Ingresos propios del restaurante: " + dinero(restauranteIngresos) + "\n"
                        + "Ingresos por domicilios:          " + dinero(domicilios) + "\n"
                        + "Total facturado:                  " + dinero(restauranteIngresos + domicilios) + "\n\n"
                        + "Solo se cuentan pedidos confirmados o entregados.\n"
                        + "Los domicilios se muestran aparte de los ingresos propios.");
    }

    private static void consultarProductoMasVendido() {
        Producto producto = restaurante.consultarProductoMasVendido();
        if (producto == null) {
            throw new OperacionCancelada("Todavia no hay ventas registradas.");
        }
        mostrarTexto("Producto mas vendido", describirProducto(producto));
    }

    private static void consultarCategoriaMayoresIngresos() {
        String categoria = restaurante.consultarCategoriaMayoresIngresos();
        if (categoria == null) {
            throw new OperacionCancelada("Todavia no hay ventas registradas.");
        }
        informar("La categoria con mayores ingresos es: " + categoria);
    }

    private static void consultarEmpresasDomicilio() {
        StringBuilder sb = new StringBuilder();
        for (EmpresaDomicilio e : restaurante.getListEmpresasDomicilio()) {
            sb.append(e.getNombre()).append("\n")
                    .append("  Tarifa base: ").append(dinero(e.getTarifaBase())).append("\n")
                    .append("  Tarifa por km: ").append(dinero(e.getTarifaKm())).append("\n")
                    .append("  Distancia maxima: ").append(numero(e.getDistanciaMaxima())).append(" km\n\n");
        }
        mostrarTexto("Empresas de domicilio", sb.toString().trim());
    }

    // =====================================================================
    // TEXTOS DESCRIPTIVOS
    // =====================================================================

    private static String describirProducto(Producto p) {
        String categoria;
        String detalle;
        if (p instanceof PlatoFuerte) {
            PlatoFuerte pf = (PlatoFuerte) p;
            categoria = "Plato fuerte";
            detalle = "Peso: " + numero(pf.getPesoGramos()) + " g | Preparacion: "
                    + pf.getTipoPreparacion().name().replace('_', ' ')
                    + " | Coccion aprobada: " + siNo(pf.isCoccionAprobada());
        } else if (p instanceof Bebida) {
            Bebida b = (Bebida) p;
            categoria = "Bebida";
            detalle = "Volumen: " + numero(b.getVolumenMl()) + " ml | Temperatura: "
                    + numero(b.getTemperaturaServicio()) + " C | " + (b.isFria() ? "Fria" : "Caliente")
                    + " | Preparacion especial: " + siNo(b.isRequierePreparacionEspecial());
        } else if (p instanceof Postre) {
            Postre po = (Postre) p;
            categoria = "Postre";
            detalle = "Peso: " + numero(po.getPesoGramos()) + " g | Temperatura: "
                    + numero(po.getTemperaturaAlmacenamiento()) + " C"
                    + " | Requiere refrigeracion: " + siNo(po.isRequiereRefrigeracion());
        } else {
            categoria = "Producto";
            detalle = "";
        }
        return "[" + categoria + "] " + p.getCodigo() + " - " + p.getNombre() + "\n"
                + "  Descripcion: " + p.getDescripcion() + "\n"
                + "  Precio base: " + dinero(p.getPrecioBase())
                + " | Precio de venta: " + dinero(p.calcularPrecioVenta()) + "\n"
                + "  Tiempo de preparacion: " + p.getTiempoPreparacion() + " min\n"
                + "  " + detalle + "\n"
                + "  Calidad: " + (p.cumpleCondicionesCalidad() ? "CUMPLE" : "NO CUMPLE");
    }

    private static String describirProductos(List<Producto> productos) {
        StringBuilder sb = new StringBuilder();
        for (Producto p : productos) {
            sb.append(describirProducto(p)).append("\n\n");
        }
        return sb.toString().trim();
    }

    private static String describirCliente(Cliente c) {
        return "Documento: " + c.getDocumento() + "\n"
                + "Nombre: " + c.getNombreCompleto() + "\n"
                + "Telefono: " + c.getTelefono() + "\n"
                + "Correo: " + c.getCorreo() + "\n"
                + "Direccion: " + c.getDireccionResidencia() + "\n"
                + "Pedidos en el historial: " + c.getListPedidos().size();
    }

    private static String describirPedido(Pedido p, double total) {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido ").append(p.getCodigo()).append("\n");
        sb.append("Fecha: ").append(p.getFecha()).append("\n");
        sb.append("Cliente: ").append(p.getTheCliente().getDocumento())
                .append(" - ").append(p.getTheCliente().getNombreCompleto()).append("\n");
        sb.append("Modalidad: ").append(nombreModalidad(p.getModalidadEntrega())).append("\n");
        sb.append("Estado: ").append(p.getEstado()).append("\n");
        if (p.getModalidadEntrega() == ModalidadEntrega.DOMICILIO) {
            if (p.isDomicilioAceptado() && p.getTheEmpresaDomicilio() != null) {
                sb.append("Domicilio: ").append(p.getTheEmpresaDomicilio().getNombre())
                        .append(" (").append(numero(p.getDistanciaKm())).append(" km)\n");
            } else {
                sb.append("Domicilio: ninguna empresa lo ha aceptado\n");
            }
        }
        sb.append("\nProductos:\n");
        if (p.getListDetalles().isEmpty()) {
            sb.append("  (sin productos)\n");
        }
        for (DetallePedido detalle : p.getListDetalles()) {
            sb.append("  - ").append(detalle.getTheProducto().getNombre())
                    .append(" x").append(detalle.getCantidad())
                    .append(" = ").append(dinero(detalle.calcularSubtotal())).append("\n");
        }
        sb.append("\nValor de los productos: ").append(dinero(p.calcularValorProductos())).append("\n");
        if (p.getModalidadEntrega() == ModalidadEntrega.DOMICILIO) {
            sb.append("Costo del domicilio:    ").append(dinero(p.calcularCostoDomicilio())).append("\n");
        }
        sb.append("VALOR TOTAL:            ").append(dinero(total));
        return sb.toString();
    }

    private static String describirPedidos(List<Pedido> pedidos) {
        StringBuilder sb = new StringBuilder();
        for (Pedido p : pedidos) {
            sb.append(p.getCodigo()).append(" | ").append(p.getFecha())
                    .append(" | ").append(p.getTheCliente().getNombreCompleto())
                    .append(" | ").append(nombreModalidad(p.getModalidadEntrega()))
                    .append(" | ").append(p.getEstado())
                    .append(" | ").append(dinero(p.calcularTotal())).append("\n");
        }
        return sb.toString().trim();
    }

    /** Historial de un cliente: codigo, fecha, estado y valor total de cada pedido. */
    private static String describirHistorial(List<Pedido> pedidos) {
        StringBuilder sb = new StringBuilder();
        for (Pedido p : pedidos) {
            sb.append("Pedido ").append(p.getCodigo())
                    .append(" | Fecha: ").append(p.getFecha())
                    .append(" | Estado: ").append(p.getEstado())
                    .append(" | Total: ").append(dinero(p.calcularTotal())).append("\n");
        }
        return sb.toString().trim();
    }

    private static String nombreModalidad(ModalidadEntrega modalidad) {
        return modalidad == ModalidadEntrega.DOMICILIO ? "Domicilio" : "Consumo en restaurante";
    }

    private static String siNo(boolean valor) {
        return valor ? "Si" : "No";
    }

    private static String dinero(double valor) {
        return "$" + FORMATO_DINERO.format(valor);
    }

    /** Numero sin ceros sobrantes: 450.0 se muestra como 450. */
    private static String numero(double valor) {
        if (valor == Math.rint(valor)) {
            return String.valueOf((long) valor);
        }
        return String.valueOf(valor);
    }

    // =====================================================================
    // ENTRADA Y SALIDA CON JOPTIONPANE
    // =====================================================================

    /** Menu desplegable. Devuelve la opcion elegida o null si se cancela. */
    private static String elegirOpcion(String titulo, String mensaje, String[] opciones) {
        Object respuesta = JOptionPane.showInputDialog(null, mensaje, TITULO + " - " + titulo,
                JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        return respuesta == null ? null : respuesta.toString();
    }

    /**
     * Lista desplegable de elementos existentes. Si la lista esta vacia o se
     * cancela, aborta la operacion.
     */
    private static <T> T elegir(String mensaje, List<T> items, Function<T, String> etiqueta,
                                String mensajeVacio, T porDefecto) {
        if (items.isEmpty()) {
            throw new OperacionCancelada(mensajeVacio);
        }
        String[] etiquetas = new String[items.size()];
        int inicial = 0;
        for (int i = 0; i < items.size(); i++) {
            etiquetas[i] = etiqueta.apply(items.get(i));
            if (items.get(i) == porDefecto) {
                inicial = i;
            }
        }
        Object respuesta = JOptionPane.showInputDialog(null, mensaje, TITULO,
                JOptionPane.QUESTION_MESSAGE, null, etiquetas, etiquetas[inicial]);
        if (respuesta == null) {
            throw new OperacionCancelada(null);
        }
        for (int i = 0; i < etiquetas.length; i++) {
            if (etiquetas[i].equals(respuesta)) {
                return items.get(i);
            }
        }
        throw new OperacionCancelada(null);
    }

    private static <E extends Enum<E>> E elegirEnum(String mensaje, E[] valores, E porDefecto) {
        return elegir(mensaje, Arrays.asList(valores), v -> v.name().replace('_', ' '), "", porDefecto);
    }

    private static String leerTexto(String mensaje, String inicial) {
        while (true) {
            Object respuesta = JOptionPane.showInputDialog(null, mensaje, TITULO,
                    JOptionPane.QUESTION_MESSAGE, null, null, inicial);
            if (respuesta == null) {
                throw new OperacionCancelada(null);
            }
            String texto = respuesta.toString().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            error("Este dato no puede estar vacio.");
        }
    }

    private static int leerEntero(String mensaje, String inicial) {
        String intento = inicial;
        while (true) {
            intento = leerTexto(mensaje, intento);
            try {
                return Integer.parseInt(intento);
            } catch (NumberFormatException e) {
                error("Ingrese un numero entero valido.");
            }
        }
    }

    private static int leerEnteroPositivo(String mensaje, String inicial) {
        String intento = inicial;
        while (true) {
            int valor = leerEntero(mensaje, intento);
            if (valor > 0) {
                return valor;
            }
            error("El valor debe ser mayor que cero.");
            intento = String.valueOf(valor);
        }
    }

    private static double leerDecimal(String mensaje, String inicial) {
        String intento = inicial;
        while (true) {
            intento = leerTexto(mensaje, intento);
            try {
                double valor = Double.parseDouble(intento.replace(',', '.'));
                if (!Double.isNaN(valor) && !Double.isInfinite(valor)) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // se informa abajo, igual que un valor no finito
            }
            error("Ingrese un numero valido (use punto o coma para los decimales, sin separador de miles).");
        }
    }

    private static double leerDecimalPositivo(String mensaje, String inicial) {
        String intento = inicial;
        while (true) {
            double valor = leerDecimal(mensaje, intento);
            if (valor > 0) {
                return valor;
            }
            error("El valor debe ser mayor que cero.");
            intento = numero(valor);
        }
    }

    private static LocalDate leerFecha(String mensaje, String inicial) {
        String intento = inicial;
        while (true) {
            intento = leerTexto(mensaje, intento);
            try {
                return LocalDate.parse(intento);
            } catch (DateTimeParseException e) {
                error("Fecha invalida. Use el formato AAAA-MM-DD (ejemplo: 2026-11-12).");
            }
        }
    }

    /** Pregunta Si/No como dato de entrada. Cerrar el dialogo cancela la operacion. */
    private static boolean leerSiNo(String mensaje, boolean porDefectoSi) {
        int respuesta = preguntar(mensaje, porDefectoSi);
        if (respuesta == JOptionPane.CLOSED_OPTION) {
            throw new OperacionCancelada(null);
        }
        return respuesta == 0;
    }

    /** @return 0 si responde Si, 1 si responde No, -1 si cierra el dialogo. */
    private static int preguntar(String mensaje, boolean porDefectoSi) {
        return JOptionPane.showOptionDialog(null, mensaje, TITULO, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new String[]{"Si", "No"}, porDefectoSi ? "Si" : "No");
    }

    private static void informar(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void error(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO + " - Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Muestra un texto largo en un area con barra de desplazamiento. */
    private static void mostrarTexto(String titulo, String texto) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(680, 380));
        JOptionPane.showMessageDialog(null, scroll, TITULO + " - " + titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}