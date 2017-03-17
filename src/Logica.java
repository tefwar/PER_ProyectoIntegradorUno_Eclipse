import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class Logica implements Observer {

	private PApplet app;
	private int color;
	private CommunicationManager com;
	private InetAddress destAddress;
	private int destPort;

	public Logica(PApplet app) {
		this.app = app;
		this.color = (int) app.random(100);

		com = new CommunicationManager();

		com.addObserver(this);
		(new Thread(this.com)).start();
		this.destPort = 6000;

		try {
			this.destAddress = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}
}
