package HistoryPhone;


// Basic object class that encapsulates object info.
public class ObjectInfo {

	// Store object information in fields.
	private String name;
	private String desc;
	private String img_url;
	private String UUID;
	
	// Object constructor initializing fields.
	public ObjectInfo(String u, String n, String d, String i) {
		name = n;
		desc = d;
		img_url = i;
		UUID = u;
	}
	
	// Getter methods for all fields.
	public String getUUID() {return UUID;}
	public String getName() {return name;}
	public String getDesc() {return desc;}
	public String getImg()  {return img_url;}

}
