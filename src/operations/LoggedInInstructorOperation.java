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

	/**
	 * present courses teacher is a tutor of then select one
	 * present students in that course and select one
	 * call helper method to get the mark
	 * prompt for adding another mark
	 */
	public void addMark(LoggedInAuthenticatedUser user) {
		InstructorModel teacher = verifyInstructor(user);
		Scanner reader = new Scanner(System.in);
		System.out.println(teacher.getName() + " " + teacher.getSurname() + " please select a course");
	
		for (ICourseOffering icourse : teacher.getIsTutorOf())
			System.out.println(icourse.getCourseName() + "\t" + icourse.getCourseID());
		System.out.println("Enter class ID: ");
		String line = reader.next().toUpperCase();
		CourseOffering course = ModelRegister.getInstance().getRegisteredCourse(line);
		
		if (course.getStudentsEnrolled().isEmpty())//no students
			return;
		
		System.out.println("Please select an enrolled student: ");
		for (StudentModel student : course.getStudentsEnrolled()) 
			System.out.println(student.getName() +" "+student.getSurname() + "\t" + student.getID());
		
		System.out.println("Enter Student ID: ");
		line = reader.next();
		StudentModel student = (StudentModel) ModelRegister.getInstance().getRegisteredUser(line);
		
		
		getMark(student, course, reader);
		

		System.out.println("Would you like to enter grades for another class/student? (y/n): ");
		System.out.println(student.getName() + " " + student.getSurname() + " has 1 new " + student.getNotificationType() + " notification");
		System.out.println();
		line = reader.next().toUpperCase();
		if (line.equalsIgnoreCase("Y"))
			addMark(user);
			
	}
	
	private void getMark(StudentModel student, CourseOffering course, Scanner reader) {
		Map<ICourseOffering, Marks> grades;
		Marks mark = new Marks();
		if (student.getPerCourseMarks() == null) { //case no marks/courses
			
			//get the possible evaluation entities and present them
			List<String> possibilities = new ArrayList<String>();
			Weights stuff = course.getEvaluationStrategies().get(student.getEvaluationEntities().get(course));
			stuff.initializeIterator();
			while (stuff.hasNext()) {
				Entry<String, Double> current = stuff.getNextEntry();
				possibilities.add(current.getKey());
				System.out.println(current.getKey());
			}
			//get user input and if it matches one of the possibilities then we add the mark and course
			System.out.println("Pick one of the above");
			String line = reader.next().toUpperCase();
			System.out.println("Enter grade: ");
			double grade = reader.nextDouble();
			mark.addToEvalStrategy(line, grade);
			for (String s : possibilities) {
				if (line.equalsIgnoreCase(s)) {
					grades = new HashMap<ICourseOffering, Marks>();
					grades.put(course, mark);
					student.setPerCourseMarks(grades);
				}
			}
		} else if (!student.getPerCourseMarks().containsKey(course)) { //we have per course marks but doesn't have the course
			List<String> possibilities = new ArrayList<String>();
			Weights stuff = course.getEvaluationStrategies().get(student.getEvaluationEntities().get(course));
			stuff.initializeIterator();
			while (stuff.hasNext()) {
				Entry<String, Double> current = stuff.getNextEntry();
				possibilities.add(current.getKey());
				System.out.println(current.getKey());
			}
			System.out.println("Pick one of the above");
			String line = reader.next().toUpperCase();
			System.out.println("Enter grade: ");
			Double grade = reader.nextDouble();
			for (String s : possibilities) {
				if (line.equalsIgnoreCase(s)) {
					mark.addToEvalStrategy(line, grade);
					student.getPerCourseMarks().put(course, mark);
				}
			}
		} else { //otherwise call the other helper
			helper(course, student, reader);

		}
		
		System.out.println("Would you like to enter another grade for the same student? (y/n)");
		String line = reader.next().toUpperCase();
		if (line.equalsIgnoreCase("Y"))
			getMark(student, course, reader);
		else
			return;
	}

	public void modifyMark(LoggedInAuthenticatedUser user) {
		InstructorModel teacher = verifyInstructor(user);
		Scanner reader = new Scanner(System.in);
	
		//select a course
		for (ICourseOffering icourse : teacher.getIsTutorOf())
			System.out.println(icourse.getCourseName() + "\t" + icourse.getCourseID());
		System.out.println("Enter class ID: ");
		String line = reader.next().toUpperCase();
		CourseOffering course = ModelRegister.getInstance().getRegisteredCourse(line);
		
		if (course.getStudentsEnrolled().isEmpty())//no students
			return;
		
		//select a student
		for (StudentModel student : course.getStudentsEnrolled()) 
			System.out.println(student.getName() +" "+student.getSurname() + "\t" + student.getID());
		System.out.println("Enter Student ID: ");
		line = reader.next();
		StudentModel student = (StudentModel) ModelRegister.getInstance().getRegisteredUser(line);
		
		helper(course, student, reader);
	}

	public void calcGrade(LoggedInAuthenticatedUser user) {
		InstructorModel teacher = verifyInstructor(user);
		Scanner reader = new Scanner(System.in);
		//select a course
		for (ICourseOffering icourse : teacher.getIsTutorOf())
			System.out.println(icourse.getCourseName() + "\t" + icourse.getCourseID());
		System.out.println("Enter class ID: ");
		String line = reader.next().toUpperCase();
		CourseOffering course = ModelRegister.getInstance().getRegisteredCourse(line);
		
		if (course.getStudentsEnrolled().isEmpty())//no students
			return;
		
		//select a student
		for (StudentModel student : course.getStudentsEnrolled()) 
			System.out.println(student.getName() +" "+student.getSurname() + "\t" + student.getID());
		System.out.println("Enter Student ID: ");
		line = reader.next();
		StudentModel student = (StudentModel) ModelRegister.getInstance().getRegisteredUser(line);
		try {
			course.calculateFinalGrade(student.getID());
		} catch (NullPointerException e) {
			System.out.println("Not all marks are present");
			return;
		}
	}

	public void printRecord(LoggedInAuthenticatedUser user) {
		InstructorModel teacher = verifyInstructor(user);
		Scanner reader = new Scanner(System.in);
		
		for (ICourseOffering icourse : teacher.getIsTutorOf())
			System.out.println(icourse.getCourseName() + "\t" + icourse.getCourseID());
		System.out.println("Enter class ID: ");
		String line = reader.next().toUpperCase();
		CourseOffering course = ModelRegister.getInstance().getRegisteredCourse(line);
		
		if (course.getStudentsEnrolled().isEmpty()) {//no students
			System.out.println("No students enrolled in this course");
			return;
		}
		System.out.println("Would you like to print the record for a specific student(0) or the whole class(1)?: ");
		int choice = reader.nextInt();
		switch (choice) {
			case 0: printSingle(course, reader);
					break;
			case 1: printAll(course);
					break;
			default:System.out.println("Single");
					printSingle(course, reader);
					break;
		}
		
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
	
	/**
	 * pretty much the same as getMark helper method but assumes percourse marks exists and has the course in it
	 * @param course
	 * @param student
	 * @param reader
	 */
	private void helper(CourseOffering course, StudentModel student, Scanner reader) {
		List<String> possibilities = new ArrayList<String>();
		Weights stuff = course.getEvaluationStrategies().get(student.getEvaluationEntities().get(course));
		stuff.initializeIterator();
		while (stuff.hasNext()) {
			Entry<String, Double> current = stuff.getNextEntry();
			possibilities.add(current.getKey());
			System.out.println(current.getKey());
		}
		
		System.out.println("Pick one of the above");
		String line = reader.next().toUpperCase();
		System.out.println("Enter grade: ");
		double grade = reader.nextDouble();
		for (String s : possibilities) {
			if (line.equalsIgnoreCase(s))
				student.getPerCourseMarks().get(course).addToEvalStrategy(line, grade);
		}
	}
	
	private void printSingle(CourseOffering course, Scanner reader) {
		for (StudentModel student : course.getStudentsEnrolled()) 
			System.out.println(student.getName() +" "+student.getSurname() + "\t" + student.getID());
		String line = reader.next();
		StudentModel student = (StudentModel) ModelRegister.getInstance().getRegisteredUser(line);
		System.out.println("------Marks for " + student.getName() + " " + student.getSurname() + " in " + course.getCourseName() + "------");
		try {
			Marks marks = student.getPerCourseMarks().get(course);
			marks.initializeIterator();
			while (marks.hasNext()) {
				Entry<String, Double> entry = marks.getNextEntry();
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
			System.out.print("Final Mark - ");
			course.calculateFinalGrade(student.getID());
			System.out.println();
		} catch (NullPointerException e) {
			System.out.println("No marks for this student");
			System.out.println();
			return;
		}
	}
	
	private void printAll(CourseOffering course) {
		for (StudentModel student : course.getStudentsEnrolled()) {
			try {
				System.out.println("------Marks for " + student.getName() + " " + student.getSurname() + " in " + course.getCourseName() + "------");
				Marks marks = student.getPerCourseMarks().get(course);
				marks.initializeIterator();
				while (marks.hasNext()) {
					Entry<String, Double> entry = marks.getNextEntry();
					System.out.println(entry.getKey() + " " + entry.getValue());
				}
				course.calculateFinalGrades();
			} catch (NullPointerException e) {
				continue;
			}
		}
	}

}
