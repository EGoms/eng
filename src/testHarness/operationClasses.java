package testHarness;

import authenticatedUsers.LoggedInAdmin;
import authenticatedUsers.LoggedInInstructor;
import authenticatedUsers.LoggedInStudent;
import authenticationServer.LogInServer;
import operations.AbstractionLayer;

//uses LoggedIn__ class methods
public class operationClasses {

	public static void main(String[] args) {
		LogInServer server = LogInServer.getServer();
		LoggedInAdmin admin = server.adminLogin();
		
		
		//methods.select gets a type of user and return the possible operations it can do then user chooses which to do
		AbstractionLayer methods = new AbstractionLayer();
		methods.select(admin);
		LoggedInInstructor instructor = (LoggedInInstructor) server.login();
		LoggedInStudent student = (LoggedInStudent) server.login();

		methods.select(student);
		methods.select(instructor);

		LoggedInStudent student2 = (LoggedInStudent) server.login();

		methods.select(admin);
	}

}
