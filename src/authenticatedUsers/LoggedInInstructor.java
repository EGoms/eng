package authenticatedUsers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import authenticationServer.AuthenticationToken;
import customDatatypes.Marks;
import offerings.CourseOffering;
import offerings.ICourseOffering;
import registrar.ModelRegister;
import systemUsers.InstructorModel;
import systemUsers.StudentModel;

public class LoggedInInstructor implements LoggedInAuthenticatedUser {

	private String name;
	private String surname;
	private String ID;
	private AuthenticationToken authenticationToken;
	private String type;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getID() {
		return ID;
	}
	
	public void setID(String iD) {
		ID = iD;
	}
	
	public AuthenticationToken getAuthenticationToken() {
		return authenticationToken;
	}
	
	public void setAuthenticationToken(AuthenticationToken authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	@Override
	public String getType() {
		return this.type = "Instructor";
	}
	
	public void addMark() {
		InstructorModel teacher = verifyInstructor(this.authenticationToken);
		Scanner reader = new Scanner(System.in);
	
		for (ICourseOffering course : teacher.getIsTutorOf())
			System.out.println(course.getCourseName() + "\t" + course.getCourseID());
		System.out.println("Enter class ID: ");
		String line = reader.next().toUpperCase();
		CourseOffering realCourse = ModelRegister.getInstance().getRegisteredCourse(line);
		
		for (StudentModel student : realCourse.getStudentsEnrolled()) {
			System.out.println(student.getName() +" "+student.getSurname() + "\t" + student.getID());
		}
		
		System.out.println("Enter Student ID: ");
		line = reader.next();
		StudentModel realStudent = (StudentModel) ModelRegister.getInstance().getRegisteredUser(line);
		
		getMark(realStudent, realCourse, reader);
		
		Marks x = realStudent.getPerCourseMarks().get(realCourse);
		x.initializeIterator();
		while (x.hasNext()) {
			Entry<String, Double> thing = x.getNextEntry();
			System.out.println(thing);
		}
			
	}
	private void getMark(StudentModel student, CourseOffering course, Scanner reader) {
		Map<ICourseOffering, Marks> grades;
		Marks mark = new Marks();
		if (student.getPerCourseMarks() == null || !student.getPerCourseMarks().containsKey(course)) {
			System.out.println("Type of assessment: \n");
			String line = reader.next();
			System.out.println("Mark: \n");
			Double grade = reader.nextDouble();
			mark.addToEvalStrategy(line, grade);
			grades = new HashMap<ICourseOffering, Marks>();
			grades.put(course, mark);
			student.setPerCourseMarks(grades);
		} else {
			System.out.println("Type of assessment: \n");
			String line = reader.next();
			System.out.println("Mark: \n");
			Double grade = reader.nextDouble();
			//mark.addToEvalStrategy(line, grade);
			student.getPerCourseMarks().get(course).addToEvalStrategy(line, grade);
		}
		System.out.println("Would you like to enter another grade? (y/n)");
		String line = reader.next().toUpperCase();
		if (line.equals("Y"))
			getMark(student, course, reader);
		else
			return;
	}
	private InstructorModel verifyInstructor(AuthenticationToken token) {
		InstructorModel teacher = null;
		if (token.getUserType().equals("Instructor")) {
			teacher = (ModelRegister.getInstance().checkIfUserHasAlreadyBeenCreated(token.getTokenID()) ? (InstructorModel) ModelRegister.getInstance().getRegisteredUser(token.getTokenID()) : null);
		}
		return teacher;
	}	
}
