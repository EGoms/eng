package operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import authenticatedUsers.LoggedInAuthenticatedUser;
import authenticatedUsers.LoggedInStudent;
import customDatatypes.Marks;
import customDatatypes.Weights;
import offerings.CourseOffering;
import offerings.ICourseOffering;
import registrar.ModelRegister;
import systemUsers.InstructorModel;
import systemUsers.StudentModel;

public class LoggedInInstructorOperation implements ILoggedInInstructorOperation {

	public void addMark(LoggedInAuthenticatedUser user) {
		InstructorModel teacher = verifyInstructor(user);
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
		
//		Marks x = realStudent.getPerCourseMarks().get(realCourse);
//		x.initializeIterator();
//		while (x.hasNext()) {
//			Entry<String, Double> thing = x.getNextEntry();
//			System.out.println(thing);
//		}
		System.out.println("Would you like to enter grades for another class/student? (y/n): ");
		line = reader.next().toUpperCase();
		if (line.equals("Y"))
			addMark(user);
			
	}
	private void getMark(StudentModel student, CourseOffering course, Scanner reader) {
		Map<ICourseOffering, Marks> grades;
		Marks mark = new Marks();
		if (student.getPerCourseMarks() == null) {
			System.out.println("Type of assessment: \n");
			String line = reader.next().toUpperCase();
			System.out.println("Mark: \n");
			Double grade = reader.nextDouble();
			mark.addToEvalStrategy(line, grade);
			grades = new HashMap<ICourseOffering, Marks>();
			student.setPerCourseMarks(grades);
			student.getPerCourseMarks().put(course, mark);
		} else if (!student.getPerCourseMarks().containsKey(course)) {
			System.out.println("Type of assessment: \n");
			String line = reader.next().toUpperCase();
			System.out.println("Mark: \n");
			Double grade = reader.nextDouble();
			mark.addToEvalStrategy(line, grade);
			student.getPerCourseMarks().put(course, mark);
		} else {
			System.out.println("Type of assessment: \n");
			String line = reader.next().toUpperCase();
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

	public void modifyMark(LoggedInAuthenticatedUser user) {
		InstructorModel teacher = verifyInstructor(user);
		Scanner reader = new Scanner(System.in);
	
		//select a course
		for (ICourseOffering course : teacher.getIsTutorOf())
			System.out.println(course.getCourseName() + "\t" + course.getCourseID());
		System.out.println("Enter class ID: ");
		String line = reader.next().toUpperCase();
		CourseOffering realCourse = ModelRegister.getInstance().getRegisteredCourse(line);
		
		//select a student
		for (StudentModel student : realCourse.getStudentsEnrolled()) 
			System.out.println(student.getName() +" "+student.getSurname() + "\t" + student.getID());
		System.out.println("Enter Student ID: ");
		line = reader.next();
		StudentModel realStudent = (StudentModel) ModelRegister.getInstance().getRegisteredUser(line);
		
		//Weights = the eval strats (assign, etc) for the course for the specific student?
		//present the possibilities for the specified evaluation strategy and have user enter one to change
		//also adds the mark if its not there
		List<String> possibilities = new ArrayList<String>();
		Weights stuff = realCourse.getEvaluationStrategies().get(realStudent.getEvaluationEntities().get(realCourse));
		stuff.initializeIterator();
		while (stuff.hasNext()) {
			Entry<String, Double> current = stuff.getNextEntry();
			possibilities.add(current.getKey());
			System.out.println(current.getKey());
		}
		
		System.out.println("Pick one of the above");
		line = reader.next().toUpperCase();
		System.out.println("Enter new grade: ");
		double grade = reader.nextDouble();
		for (String s : possibilities) {
			if (line.equalsIgnoreCase(s))
				realStudent.getPerCourseMarks().get(realCourse).addToEvalStrategy(line, grade);
		}
		//realStudent.getPerCourseMarks().get(realCourse).addToEvalStrategy(line, grade);
	}

	@Override
	public void calcGrade(LoggedInAuthenticatedUser user) {
		//call course . calculate grade
		
	}

	@Override
	public void printRecord(LoggedInAuthenticatedUser user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printIndividualRecord(LoggedInAuthenticatedUser user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printMultiple(LoggedInAuthenticatedUser user) {
		// TODO Auto-generated method stub
		
	}
	
	private InstructorModel verifyInstructor(LoggedInAuthenticatedUser user) {
		InstructorModel teacher = null;
		if (user.getAuthenticationToken().getUserType().equals("Instructor")) {
			teacher = (ModelRegister.getInstance().checkIfUserHasAlreadyBeenCreated(user.getID()) ? (InstructorModel) ModelRegister.getInstance().getRegisteredUser(user.getID()) : null);
		}
		return teacher;
	}
	
	private StudentModel verifyStudent(LoggedInAuthenticatedUser user) {
		StudentModel student = null;
		if (user.getAuthenticationToken().getUserType().equals("Student")) {
			student = ModelRegister.getInstance().checkIfUserHasAlreadyBeenCreated(user.getID()) ? (StudentModel) ModelRegister.getInstance().getRegisteredUser(user.getID()) : null;
		}
		return student;
	}

}
