package systemUsers;

import java.util.List;
import offerings.ICourseOffering;

public class AdminModel implements IAdminModel{
	
	private String name;
	private String surname;
	private String ID;
	private List<ICourseOffering> course;
	private String type;
	
	public String getType() {
		return this.type = "Admin";
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getID() {
		return ID;
	}
	
	public void setID(String iD) {
		ID = iD;
	}
	public void setCourses(List<ICourseOffering> courses) {
		this.course = courses;
	}
	public List<ICourseOffering> getCourses() {
		return course;
	}

}
