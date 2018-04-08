package testHarness;

import authenticatedUsers.LoggedInAdmin;
import authenticatedUsers.LoggedInStudent;
import authenticationServer.LogInServer;
import operations.AbstractionLayer;

public class classMethods {

	public static void main(String[] args) {
		LogInServer server = LogInServer.getServer();
		LoggedInAdmin admin = server.adminLogin();
		
		
		//this one logs in many users and then performs the specified operation on each of the matching user types
		
		AbstractionLayer methods = new AbstractionLayer();
		methods.start(admin);
		server.loginMany();
		methods.enroll(); //calls enroll on all logged in students
		methods.addMark();
		methods.printRecord(); //student print record
		methods.printClass(); //instructor method
		methods.enroll();
		methods.addMark(); //instructor
		methods.printRecord();
		methods.stop(admin);

	}

}
