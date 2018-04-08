package testHarness;

import java.io.IOException;

import authenticatedUsers.LoggedInAdmin;
import authenticatedUsers.LoggedInInstructor;
import authenticatedUsers.LoggedInStudent;
import authenticationServer.LogInServer;
import operations.LoggedInAdminOperation;
import operations.LoggedInInstructorOperation;
import operations.LoggedInStudentOperation;

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
		adminOp.stop(admin);
	}

}
