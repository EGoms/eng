package loggedInUserFactory;

import authenticatedUsers.LoggedInAdmin;
import authenticatedUsers.LoggedInAuthenticatedUser;
import authenticatedUsers.LoggedInInstructor;
import authenticatedUsers.LoggedInStudent;
import authenticationServer.AuthenticationToken;
import registrar.ModelRegister;

public class LoggedInUserFactory {

	public LoggedInUserFactory(){
		
	}
	
	public LoggedInAuthenticatedUser createAuthenticatedUser(AuthenticationToken authenticationToken){
		switch(authenticationToken.getUserType()){
		case "Admin":
			return createLoggedInAdmin(authenticationToken);
		case "Student":
			return createLoggedInStudent(authenticationToken);
		case "Instructor":
			return createLoggedInInstructor(authenticationToken);
		default:
			return null;
		}
	}
	
	public LoggedInStudent createLoggedInStudent(AuthenticationToken authenticationToken){
		LoggedInStudent student = new LoggedInStudent();
		String id = authenticationToken.getTokenID();
		student.setAuthenticationToken(authenticationToken);
		student.setID(id);
		student.setName(ModelRegister.getInstance().getRegisteredUser(id).getName());
		student.setSurname(ModelRegister.getInstance().getRegisteredUser(id).getSurname());
		return student;
	}
	
	public LoggedInAdmin createLoggedInAdmin(AuthenticationToken authenticationToken){
		LoggedInAdmin admin = new LoggedInAdmin();
		String id = authenticationToken.getTokenID();
		admin.setAuthenticationToken(authenticationToken);
		admin.setID(id);
		admin.setName("Admin");
		//admin.setSurname(ModelRegister.getInstance().getRegisteredUser(id).getSurname());
		return admin;
	}
	
	public LoggedInInstructor createLoggedInInstructor(AuthenticationToken authenticationToken){
		LoggedInInstructor instructor = new LoggedInInstructor();
		String id = authenticationToken.getTokenID();
		instructor.setAuthenticationToken(authenticationToken);
		instructor.setID(id);
		instructor.setName(ModelRegister.getInstance().getRegisteredUser(id).getName());
		instructor.setSurname(ModelRegister.getInstance().getRegisteredUser(id).getSurname());
		return instructor;

	}
	
}
