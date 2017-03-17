import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Observable;

public class CommunicationManager extends Observable implements Runnable {
	private MulticastSocket multi;
	private final int PORT = 5000;
	private InetAddress grupo;
	private int identifier, cantidad =0;
	private boolean identified;

	// -------------------------------------------------------------------
	public CommunicationManager() {
		try {
			multi = new MulticastSocket(PORT);
			grupo = InetAddress.getByName("224.2.2.3");
			multi.joinGroup(grupo);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		unirse();

		try {
			multi.setSoTimeout(2000);
			while (!identified) {
				rGrupo();
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// -------------------------------------------------------------------
	//Saluda
	private void unirse() {
		AutoIDMessage message = new AutoIDMessage("nuevo c:");
		
		byte[] bytes = serialize(message);
		try {
			sendMessage(bytes, grupo, PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------
	//Respuesta del grupo
	private void rGrupo() throws IOException {
		try {
			DatagramPacket receivedPacket = receiveMessage();
			Object receivedObject = deserialize(receivedPacket.getData());

			// Ayuda de Andres GC
			if (receivedObject instanceof AutoIDMessage) {
				AutoIDMessage message = (AutoIDMessage) receivedObject;
				String messageContent = message.getContent();

				if (messageContent.contains("Holi soy:")) {
					

					String[] partes = messageContent.split(":");
					int externalID = Integer.parseInt(partes[1]);
					if (externalID >= identifier) {
						identifier = externalID + 1;
					}
				}
			}

		} catch (SocketTimeoutException e) {
			if (identifier == 0) {
				identifier = 1;
			}
			identified = true;
			multi.setSoTimeout(0);
		}

	}

	// -------------------------------------------------------------------
	@Override
	public void run() {
		while (true) {
			if (multi != null) {
				try {
					DatagramPacket Pack = receiveMessage();
					Object objeto = deserialize(Pack.getData());

					if (objeto != null) {
						if (objeto instanceof AutoIDMessage) {
							AutoIDMessage mensaje = (AutoIDMessage) objeto;
							String messageContent = mensaje.getContent();

							if (messageContent.contains("nuevo")) {
								identiG();
							}
						}
						setChanged();
						notifyObservers(objeto);
						clearChanged();
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// -------------------------------------------------------------------
	// Identifica quienes hay para no repetir id
	private void identiG() {
		AutoIDMessage message = new AutoIDMessage("Holi soy:" + identifier);
		byte[] bytes = serialize(message);
		try {
			sendMessage(bytes, grupo, PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// -------------------------------------------------------------------
	//Deserializar
	private Object deserialize(byte[] bytes) {
			Object data = null;
			try {
				ByteArrayInputStream bytein = new ByteArrayInputStream(bytes);
				ObjectInputStream inp = new ObjectInputStream(bytein);
				data = inp.readObject();

				// Cerrar siempre
				inp.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return data;
		}

	// -------------------------------------------------------------------
	//Serializar
	private byte[] serialize(Object data) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream byteout = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteout);
			out.writeObject(data);
			bytes = byteout.toByteArray();

			// Cerrar siempre
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}


	// -------------------------------------------------------------------
	//Enviar mensaje
	public void sendMessage(byte[] data, InetAddress destAddress, int destPort) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length, destAddress, destPort);
		multi.send(packet);
		// mensajeEnviado
	}

	// -------------------------------------------------------------------
	//Recibir mensaje
	public DatagramPacket receiveMessage() throws IOException {
		byte[] buffer = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		multi.receive(packet);
		return packet;

	}
	
	// -------------------------------------------------------------------
	public void enviar(Object mensajito){
		try {
			byte[] bytes = serialize(mensajito);
			sendMessage(bytes, grupo, PORT);
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	// -------------------------------------------------------------------
	public int getIdentifier() {
		return this.identifier;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
}