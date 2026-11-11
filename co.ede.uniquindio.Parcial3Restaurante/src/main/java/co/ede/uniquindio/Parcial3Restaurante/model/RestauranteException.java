package co.ede.uniquindio.Parcial3Restaurante.model;

/**
 * Excepcion base de todas las reglas de negocio del restaurante.
 * Es una excepcion verificada (checked): obliga a quien llama al metodo
 * a atenderla con try/catch o a declararla con throws.
 */
public class RestauranteException extends Exception {

    public RestauranteException(String mensaje) {
        super(mensaje);
    }
}