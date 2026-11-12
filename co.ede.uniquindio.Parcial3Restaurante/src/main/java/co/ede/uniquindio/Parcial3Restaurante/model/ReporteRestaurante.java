package co.ede.uniquindio.Parcial3Restaurante.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resumen de ventas del restaurante. Solo cuenta los pedidos que son venta
 * ({@link Pedido#esVenta()}: CONFIRMADO o ENTREGADO).
 *
 * @param ingresosRestaurante       ingresos propios: solo productos, sin domicilios.
 * @param ingresosDomicilios        total cobrado por domicilios.
 * @param productoMasVendido        producto con mas unidades vendidas (null si no hay ventas).
 * @param categoriaMayoresIngresos  categoria con mayores ingresos por productos (null si no hay ventas).
 */
public record ReporteRestaurante(double ingresosRestaurante,
                                 double ingresosDomicilios,
                                 Producto productoMasVendido,
                                 String categoriaMayoresIngresos) {

    /** Ingresos propios mas ingresos por domicilios. */
    public double totalFacturado() {
        return ingresosRestaurante + ingresosDomicilios;
    }

    /**
     * Calcula el reporte a partir de los pedidos, contando solo las ventas.
     * En empate de unidades gana el primer producto vendido; en empate de
     * ingresos por categoria gana PlatoFuerte, luego Bebida, luego Postre.
     */
    public static ReporteRestaurante generar(List<Pedido> pedidos) {
        double ingresosRestaurante = 0;
        double ingresosDomicilios = 0;
        boolean hayVentas = false;

        Map<String, Integer> unidadesPorCodigo = new LinkedHashMap<>();
        Map<String, Producto> productoPorCodigo = new LinkedHashMap<>();

        Map<String, Double> ingresosPorCategoria = new LinkedHashMap<>();
        ingresosPorCategoria.put("PlatoFuerte", 0.0);
        ingresosPorCategoria.put("Bebida", 0.0);
        ingresosPorCategoria.put("Postre", 0.0);

        for (Pedido pedido : pedidos) {
            if (!pedido.esVenta()) {
                continue;
            }
            ingresosRestaurante += pedido.calcularValorProductos();
            ingresosDomicilios += pedido.calcularCostoDomicilio();
            for (DetallePedido detalle : pedido.getListDetalles()) {
                Producto producto = detalle.getTheProducto();
                if (producto == null) {
                    continue;
                }
                hayVentas = true;
                unidadesPorCodigo.merge(producto.getCodigo(), detalle.getCantidad(), Integer::sum);
                productoPorCodigo.putIfAbsent(producto.getCodigo(), producto);
                ingresosPorCategoria.merge(producto.getCategoria(), detalle.calcularSubtotal(), Double::sum);
            }
        }

        Producto masVendido = null;
        int maxUnidades = 0;
        for (Map.Entry<String, Integer> entrada : unidadesPorCodigo.entrySet()) {
            if (entrada.getValue() > maxUnidades) {
                maxUnidades = entrada.getValue();
                masVendido = productoPorCodigo.get(entrada.getKey());
            }
        }

        String categoria = null;
        if (hayVentas) {
            double maxIngresos = Double.NEGATIVE_INFINITY;
            for (Map.Entry<String, Double> entrada : ingresosPorCategoria.entrySet()) {
                if (entrada.getValue() > maxIngresos) {
                    maxIngresos = entrada.getValue();
                    categoria = entrada.getKey();
                }
            }
        }

        return new ReporteRestaurante(ingresosRestaurante, ingresosDomicilios, masVendido, categoria);
    }
}
