package operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import authenticatedUsers.LoggedInAuthenticatedUser;
import offerings.CourseOffering;
import offerings.ICourseOffering;
import offerings.OfferingFactory;
import registrar.ModelRegister;
import systemUsers.StudentModel;

public class LoggedInAdminOperation implements ILoggedInAdminOperation {

	@Override
	public void start(LoggedInAuthenticatedUser user) throws IOException {
		if (user.getAuthenticationToken().getUserType() != "Admin") {
			System.out.println("You are not an admin");
			return;
		}
		OfferingFactory factory = new OfferingFactory();
		BufferedReader reader = new BufferedReader(new FileReader(new File("note_1.txt")));
		CourseOffering course =  factory.createCourseOffering(reader);
		reader.close();
		reader = new BufferedReader(new FileReader(new File("note_2.txt")));
		course = factory.createCourseOffering(reader);
		reader.close();
		
		for(CourseOffering course1 : ModelRegister.getInstance().getAllCourses()){
			System.out.println("ID : " + course1.getCourseID() + "\nCourse name : " + course1.getCourseName() + "\nSemester : " + 
			course1.getSemester());
			System.out.println("Students allowed to enroll\n");
			for(StudentModel student : course1.getStudentsAllowedToEnroll()){
				System.out.println("Student name : " + student.getName() + "\nStudent surname : " + student.getSurname() + 
						"\nStudent ID : " + student.getID() + "\nStudent EvaluationType : " + 
						student.getEvaluationEntities().get(course1) + "\n\n");
			}
			
			for(StudentModel student : course1.getStudentsAllowedToEnroll()){
				for(ICourseOffering course2 : student.getCoursesAllowed())
				System.out.println(student.getName() + "\t\t -> " + course2.getCourseName());
			}
		}
		
	}

	@Override
	public void stop(LoggedInAuthenticatedUser user) {
		// TODO Auto-generated method stub
		
	}

}
