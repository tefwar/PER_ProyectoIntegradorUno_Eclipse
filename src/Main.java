import processing.core.PApplet;

public class Main extends PApplet {
	private Logica app;

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		PApplet.main("Main");
	}

	public void settings() {
		size(300, 300);
	}

	public void setup() {
		colorMode(HSB, 100);
		app = new Logica(this);
	}

	public void draw() {
		background(100);
		//app.inicio();
		
	}
	
	public void mouseDragged(){
		//app.arrastrar();
	}
	
	public void mousePressed(){
		//app.clic();
	}
}
