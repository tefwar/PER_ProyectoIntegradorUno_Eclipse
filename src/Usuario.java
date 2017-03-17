import java.io.Serializable;

public class Usuario implements Serializable {
	
	private String email;
	private int nombre, contrasena;
	private boolean registrado;
		
	//Usuario Nuevo
	public Usuario(int nombre, int contrasena, String email, boolean registrado) {
		super();
		this.nombre = nombre;
		this.contrasena = contrasena;
		this.registrado = registrado;
	}
	
	//Usuario registrado
	public Usuario(int nombre, int contrasena, boolean registrado) {
		super();
		this.nombre = nombre;
		this.contrasena = contrasena;
		this.registrado = registrado;
	}

}
