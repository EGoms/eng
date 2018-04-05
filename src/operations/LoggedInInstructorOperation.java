package operations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import authenticatedUsers.LoggedInAuthenticatedUser;
import authenticatedUsers.LoggedInStudent;
import customDatatypes.Marks;
import offerings.CourseOffering;
import offerings.ICourseOffering;
import registrar.ModelRegister;
import systemUsers.InstructorModel;
import systemUsers.StudentModel;

public class LoggedInInstructorOperation implements ILoggedInInstructorOperation {

	@Override
	public void addMark(LoggedInAuthenticatedUser user) {
		InstructorModel teacher = verifyInstructor(user);
		Scanner reader = new Scanner(System.in);
		Map<ICourseOffering, Marks> grades;
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
		
		System.out.println("Type of assessment: \n");
		line = reader.next();
		System.out.println("Mark: \n");
		Double mark = reader.nextDouble();
		Marks grade = new Marks();
		grade.addToEvalStrategy(line, mark);
		//add more marks if there are more to add?
		//then put course, marks in students set per course marks?
		
		if (realStudent.getPerCourseMarks() == null) {
			//System.out.println("problem");
			grades = new HashMap<ICourseOffering, Marks>();
			grades.put(realCourse, grade);
			realStudent.setPerCourseMarks(grades);
		}
		System.out.println(realStudent.getPerCourseMarks());
	}
	public void addMark(LoggedInAuthenticatedUser user, LoggedInAuthenticatedUser user1) {
		InstructorModel teacher = verifyInstructor(user);
		StudentModel student = verifyStudent(user1);
		Scanner reader = new Scanner(System.in);
		Map<ICourseOffering, Marks> grades;
		List<ICourseOffering> courses = teacher.getIsTutorOf();
		if (courses == null) {
			System.out.println("Not teaching any courses");
			return;
		}
		for (ICourseOffering course : courses) { //for the courses the teacher is a tutor of
			if (course.getStudentsEnrolled().contains(student)) { //if the student is enrolled then
				
				if (student.getPerCourseMarks() == null) {
					grades = new HashMap<ICourseOffering, Marks>();
					grades.put(course, null);
					student.setPerCourseMarks(grades);
				}
				
				//student.getPerCourseMarks().put(course, null);
				System.out.println("Type of assessment: \n");
				String line = reader.next();
				System.out.println("Mark: \n");;
				Double mark = reader.nextDouble();
				
				if (student.getPerCourseMarks().containsKey(course));
					Marks forCourse = student.getPerCourseMarks().get(course);
					if (forCourse != null) {
						forCourse.initializeIterator();
						while (forCourse.hasNext()) {
							if (forCourse.getCurrentKey().equals(line))
								System.out.println("Mark already present");
							else {
								forCourse.addToEvalStrategy(line, mark);
							}
						}
					} else {
						student.getPerCourseMarks().get(course).addToEvalStrategy(line, mark);
					}
				 
				//then otherwise the course isn't in so add the course too?
				//add to student percourse marks at the course a <String, Double> if string not already in
			}
		}
		
	}

	@Override
	public void modifyMark(LoggedInAuthenticatedUser user) {
		//query a student in a class and a specific mark entry and edit it
		
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
