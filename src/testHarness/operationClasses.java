package testHarness;

import authenticatedUsers.LoggedInAdmin;
import authenticatedUsers.LoggedInInstructor;
import authenticatedUsers.LoggedInStudent;
import authenticationServer.LogInServer;
import operations.AbstractionLayer;

/**
 * Single log in
 * Uses the class methods of logged in user objects to carry out functionality.
 * AbstractionLayer has methods to select an operation based on input parameter of a logged in user
 * AbstractionLayer also has mthods that perform a specific operation on all users of a valid type
 * @author evangomolin
 *
 */
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
		methods.select(student);

		LoggedInStudent student2 = (LoggedInStudent) server.login();
		methods.select(student2);
		methods.select(instructor);
		methods.select(student2);

		methods.select(admin);
	}

}
