package operations;

import authenticatedUsers.LoggedInAuthenticatedUser;
import authenticatedUsers.LoggedInStudent;
import registrar.ModelRegister;
import systemUsers.StudentModel;

public class EnrollOperation implements IOperation {

	@Override
	public void operate(LoggedInAuthenticatedUser user) {
		if (user.getAuthenticationToken().getUserType() != "Student")
			return; 
		
		StudentModel target = null;
		if (ModelRegister.getInstance().checkIfUserHasAlreadyBeenCreated(user.getID()))
			target = (StudentModel) ModelRegister.getInstance().getRegisteredUser(user.getID());
		
		System.out.println(target.getCoursesAllowed());
		System.out.println(target.getCoursesEnrolled());
		
	}

}
