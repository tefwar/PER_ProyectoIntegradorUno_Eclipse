import java.io.Serializable;

public class Producto implements Serializable {
	
	private String nombre;
	private int precio, cantidad;
	
	public Producto(String nombre, int precio, int cantidad) {
		super();
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantidad;
	}
}
