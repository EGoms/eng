package testHarness;

import java.io.IOException;

import authenticatedUsers.LoggedInAdmin;
import authenticatedUsers.LoggedInInstructor;
import authenticatedUsers.LoggedInStudent;
import authenticationServer.LogInServer;
import operations.LoggedInAdminOperation;
import operations.LoggedInInstructorOperation;
import operations.LoggedInStudentOperation;

/**
 * This version uses the operations package which has the functionality of the system split by the distinct (logged in) user types
 * The classes which implement the functionality require a logged in user as parameter to call the methods.
 * @author evangomolin
 *
 */
public class TestMain {

	public static void main(String[] args) throws IOException {
		LogInServer server = LogInServer.getServer();
		LoggedInAdmin admin = server.adminLogin();
		
		LoggedInAdminOperation adminOp = new LoggedInAdminOperation();
		LoggedInInstructorOperation instructorOp = new LoggedInInstructorOperation();
		LoggedInStudentOperation studentOp = new LoggedInStudentOperation();
		
		adminOp.start(admin);
		
		LoggedInInstructor instructor = (LoggedInInstructor) server.login();
		LoggedInStudent s1 = (LoggedInStudent) server.login();
		
		studentOp.enroll(s1);
		instructorOp.addMark(instructor);
		studentOp.printRecord(s1);
		instructorOp.printRecord(instructor);
		
		
		LoggedInStudent s2 = (LoggedInStudent) server.login();
		studentOp.enroll(s2);
		instructorOp.addMark(instructor);
		studentOp.printRecord(s2);
		instructorOp.printRecord(instructor);
		adminOp.stop(admin);
	}

}
