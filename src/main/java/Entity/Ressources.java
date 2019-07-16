package Entity;
import com.google.cloud.firestore.annotation.Exclude;

public class Ressources {

	private String id;
	
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Ressources(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Ressources() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
