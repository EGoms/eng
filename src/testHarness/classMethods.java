package testHarness;

import authenticatedUsers.LoggedInAdmin;
import authenticatedUsers.LoggedInStudent;
import authenticationServer.LogInServer;

public class classMethods {

	public static void main(String[] args) {
		LogInServer server = LogInServer.getServer();
		LoggedInAdmin admin = server.adminLogin();
		
		AbstractionLayer methods = new AbstractionLayer();
		methods.start(admin);
		//server.login();
		//methods.enroll();
		LoggedInStudent student = (LoggedInStudent) server.login("3456", "pass");
		methods.enroll(student);
		methods.stop(admin);

	}

}
