package operations;

import java.util.ArrayList;
import java.util.Scanner;

import authenticatedUsers.LoggedInAuthenticatedUser;
import authenticatedUsers.LoggedInStudent;
import customDatatypes.EvaluationTypes;
import customDatatypes.NotificationTypes;
import registrar.ModelRegister;
import systemUsers.StudentModel;
import offerings.CourseOffering;
import offerings.ICourseOffering;
/**
 * 
 * @author evangomolin
 * 
 */

public class LoggedInStudentOperation implements ILoggedInStudentOperation{
	

	public void enroll(LoggedInAuthenticatedUser user) {
		StudentModel target = verifyStudent(user);
		System.out.println(target.getName() +" "+target.getSurname());
		Scanner reader = new Scanner(System.in);
		
		if (target.getCoursesEnrolled() == null) {
			target.setCoursesEnrolled(new ArrayList<ICourseOffering>());
		}
		for (ICourseOffering icourse : target.getCoursesAllowed()) {
			System.out.println("Do you wish to enroll in: " +  icourse.getCourseName() + "?");
			System.out.println("1 for yes, 0 for no");
			int line = reader.nextInt();
			if (line == 0)
				continue;
			System.out.println("Select Enrollment status: \n1) FC \n2) FA \n3) PC \n4) PA");
			line = reader.nextInt();
			EvaluationTypes type;
			switch (line) {
				case 1: type = EvaluationTypes.FULL_CREDIT;
						break;
				case 2: type = EvaluationTypes.FULL_AUDIT;
						break;
				case 3: type = EvaluationTypes.PART_CREDIT;
						break;
				case 4: type = EvaluationTypes.PART_AUDIT;
						break;
				default: type = EvaluationTypes.FULL_CREDIT;
						break;
			}
			if (!target.getCoursesEnrolled().contains(icourse)) {
				target.getEvaluationEntities().put(icourse, type);
				target.getCoursesEnrolled().add(icourse);
				icourse.getStudentsEnrolled().add(target);
			}
		}
	}

	@Override
	public void selectNotification(LoggedInAuthenticatedUser user) {
		NotificationTypes type = NotificationTypes.PIGEON_POST; //default
		StudentModel target = verifyStudent(user);
		//if (ModelRegister.getInstance().checkIfUserHasAlreadyBeenCreated(user.getID()))
		//	target = (StudentModel) ModelRegister.getInstance().getRegisteredUser(user.getID());
		if (target == null) {
			System.out.println("User does not exist");
			return;
		}
		System.out.println("Select notification: \n1) Email \n2) Cellphone \n3)Pigeon Post");
		Scanner reader = new Scanner(System.in);
		int line = reader.nextInt();
		
		switch (line) {
			case 1: type = NotificationTypes.EMAIL;
					break;
			case 2: type = NotificationTypes.CELLPHONE;
					break;
			case 3: type = NotificationTypes.PIGEON_POST;
					break;
			default: type = NotificationTypes.PIGEON_POST;
					break;
			}
		
		target.setNotificationType(type);
		System.out.println(target.getNotificationType());
	}

	@Override
	public void addNotification(LoggedInAuthenticatedUser user, NotificationTypes notification) {
		StudentModel target = verifyStudent(user);
		if (target == null) {
			System.out.println("User not in Register");
			return;
		}
		target.setNotificationType(notification);
		
	}

	@Override
	public void printRecord(LoggedInAuthenticatedUser user) {
		StudentModel target = verifyStudent(user);
		

		for (ICourseOffering course : target.getCoursesEnrolled()) {
			System.out.println(target.getPerCourseMarks());
		}
		
	}
	
	private StudentModel verifyStudent(LoggedInAuthenticatedUser user) {
		StudentModel student = null;
		if (user.getAuthenticationToken().getUserType().equals("Student")) {
			student = ModelRegister.getInstance().checkIfUserHasAlreadyBeenCreated(user.getID()) ? (StudentModel) ModelRegister.getInstance().getRegisteredUser(user.getID()) : null;
		}
		return student;
	}
	
	
}
