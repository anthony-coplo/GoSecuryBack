package Entity;

import com.google.cloud.firestore.annotation.Exclude;

public class Image {

	private String id;
	
	private String name;
	
	private String url;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Image(String id, String name, String url) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
	}

	public Image() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	
}
