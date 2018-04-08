package testHarness;

import authenticatedUsers.LoggedInAdmin;
import authenticatedUsers.LoggedInInstructor;
import authenticatedUsers.LoggedInStudent;
import authenticationServer.LogInServer;
import operations.AbstractionLayer;

public class operationClasses {

	public static void main(String[] args) {
		LogInServer server = LogInServer.getServer();
		LoggedInAdmin admin = server.adminLogin();
		
		AbstractionLayer methods = new AbstractionLayer();
		//methods.start(admin);
		methods.select(admin);
		LoggedInInstructor instructor = (LoggedInInstructor) server.login();
		LoggedInStudent student = (LoggedInStudent) server.login();
		//methods.enroll(student);
		//methods.addMark(instructor);
		methods.select(student);
		methods.select(instructor);
//		methods.printRecord(student);
//		methods.printClass(instructor);
//		
		LoggedInStudent student2 = (LoggedInStudent) server.login();
//		methods.enroll(student2);
//		methods.addMark(instructor);
//		methods.printRecord(student2);
//		methods.printClass(instructor);
//		
//		methods.stop(admin);
		methods.select(admin);
	}

}
