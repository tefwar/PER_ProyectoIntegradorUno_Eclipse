
import java.io.Serializable;

public class AutoIDMessage implements Serializable{

	private String c;
	
	public AutoIDMessage(String s){
		this.c = s;
	}

	public String getContent() {
		return c;
	}	
}