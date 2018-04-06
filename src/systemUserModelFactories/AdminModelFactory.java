package systemUserModelFactories;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import offerings.CourseOffering;
import offerings.ICourseOffering;
import registrar.ModelRegister;
import systemUsers.AdminModel;


public class AdminModelFactory implements SystemUserModelFactory {

	/**
	 * this method requires that the first file object read must have the last line be the admin
	 */
	public AdminModel createSystemUserModel(BufferedReader br, ICourseOffering course) {
		AdminModel admin;
		try {
			String line = br.readLine();
			if(!ModelRegister.getInstance().checkIfUserHasAlreadyBeenCreated(line.split("\t")[2])) {
				admin = new AdminModel();
				admin.setName(line.split("\t")[0]);
				admin.setSurname(line.split("\t")[1]);
				admin.setID(line.split("\t")[2]);
				List<ICourseOffering> courseList = new ArrayList<ICourseOffering>();
				admin.setCourses(courseList);
				ModelRegister.getInstance().registerUser(admin.getID(), admin);
			}
			admin = (AdminModel) ModelRegister.getInstance().getRegisteredUser("0000");
			if (admin.getCourses().contains(course)) {
				admin.getCourses().add(course);
			}
			return admin;
		} catch(IOException ioe){
			System.out.println(ioe.getMessage() + "exception thrown at StudentModelCreation"); 
			return null;
		}

	}
	/**
	 * this method does not require admin to be specified in a file instead it automatically creates an admin and links all courses
	 * to it
	 * @param course - to link to the admin
	 * @return AdminModel with the class added to list of classes it oversees
	 */
	public AdminModel createAdmin() {
		AdminModel admin;
		if (!ModelRegister.getInstance().checkIfUserHasAlreadyBeenCreated("0000")) {
			admin = new AdminModel();
			admin.setName("Evan");
			admin.setSurname("Gomolin");
			admin.setID("0000");
			List<ICourseOffering> courseList = new ArrayList<ICourseOffering>();
			admin.setCourses(courseList);
			ModelRegister.getInstance().registerUser(admin.getID(), admin);
			return admin;
		}
		admin = (AdminModel) ModelRegister.getInstance().getRegisteredUser("0000");
		return admin;
	}

//---------------------------------------------- for testing admin model factory
	public static void main(String[] args){
//		AdminModelFactory factory = new AdminModelFactory();
//		AdminModel am = factory.createSystemUserModel(null, null);
//		am.getID();
	}

}

