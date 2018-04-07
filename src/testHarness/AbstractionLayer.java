package testHarness;
/**
 * This file contains methods that call the methods implemented in both the AuthenticatedUser classes and the classes in the 
 * Operations package. Either can be used, they both accomplish the same functionality. Function calls are easier with the class methods
 * This file has overloaded methods for each to call each method for a single logged in user or all logged in users
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import authenticatedUsers.LoggedInAuthenticatedUser;
import authenticatedUsers.LoggedInAdmin;
import authenticatedUsers.LoggedInInstructor;
import authenticatedUsers.LoggedInStudent;
import authenticationServer.AuthenticationToken;
import authenticationServer.LogInServer;
import loggedInUserFactory.LoggedInUserFactory;
import offerings.CourseOffering;
import offerings.ICourseOffering;
import offerings.OfferingFactory;
import operations.LoggedInAdminOperation;
import operations.LoggedInInstructorOperation;
import operations.LoggedInStudentOperation;
import registrar.ModelRegister;
import systemUsers.AdminModel;
import systemUsers.StudentModel;
import customDatatypes.NotificationTypes;
import systemUsers.InstructorModel;
import operations.LoggedInStudentOperation;

public class AbstractionLayer {
	public void enroll() {
		for (LoggedInStudent student : LogInServer.getServer().getLoggedInStudents())
			student.enroll();
	}
	
	private void enroll(LoggedInStudent student) {
		student.enroll();
	}
	
	public void addNotification(NotificationTypes type) {
		for (LoggedInStudent student : LogInServer.getServer().getLoggedInStudents())
			student.addNotification(type);
	}
	
	private void addNotification(LoggedInStudent student, NotificationTypes type) {
		student.addNotification(type);
	}
	
	private void selectNotification(LoggedInStudent student) {
		student.selectNotification();
	}
	public void selectNotification() {
		for (LoggedInStudent student : LogInServer.getServer().getLoggedInStudents())
			student.selectNotification();
	}
	
	public void printRecord() {
		for (LoggedInStudent student : LogInServer.getServer().getLoggedInStudents())
			student.printRecord();
	}
	
	private void printRecord(LoggedInStudent student) {
		student.printRecord();
	}
	
	public void start(LoggedInAdmin admin) {
		try {
			admin.start();
		} catch (IOException e) {
			System.out.println("Missing file");
			e.printStackTrace();
		}
	}
	public void restart(LoggedInAdmin admin) {
		try {
			admin.restart();
		} catch (IOException e) {
			System.out.println("Missing file");
			e.printStackTrace();
		}
	}
	public void stop(LoggedInAdmin admin) {
		try {
			admin.stop();
		} catch (IOException e) {
			System.out.println("File error");
			e.printStackTrace();
		}
	}
	
	public void addMark() {
		for (LoggedInInstructor instructor : LogInServer.getServer().getLoggedInInstructors())
			instructor.addMark();
	}
	public void modifyMark() {
		for (LoggedInInstructor instructor : LogInServer.getServer().getLoggedInInstructors())
			instructor.modifyMark();
	}
	public void calcGrade() {
		for (LoggedInInstructor instructor : LogInServer.getServer().getLoggedInInstructors())
			instructor.calcGrade();
	}
	public void printClass() {
		for (LoggedInInstructor instructor : LogInServer.getServer().getLoggedInInstructors())
			instructor.printRecord();
	}
	
	private void addMark(LoggedInInstructor instructor) {
		instructor.addMark();
	}
	private void modifyMark(LoggedInInstructor instructor) {
		instructor.modifyMark();
	}
	private void calcGrade(LoggedInInstructor instructor) {
		instructor.calcGrade();
	}
	private void printClass(LoggedInInstructor instructor) {
		instructor.printRecord();
	}
	
	private void instructor(LoggedInInstructor instructor) {
		Scanner reader = new Scanner(System.in);
		System.out.println("1) add Mark \n2) modifyMark \n3) calculate Grade \n4) print Record\n");
		int choice = reader.nextInt();
		switch (choice) {
		case 1: addMark(instructor);
				break;
		case 2: modifyMark(instructor);
				break;
		case 3: calcGrade(instructor);
				break;
		case 4: printClass(instructor);
				break;
		default: System.out.println("Try again");
				instructor(instructor);
				break;
		}
	}
	private void student(LoggedInStudent student) {
		Scanner reader = new Scanner(System.in);
		System.out.println("1) enroll \n2) select Notification \n3) add Notification \n4) print Record\n");
		int choice = reader.nextInt();
		switch (choice) {
		case 1: enroll(student);
				break;
		case 2: selectNotification(student);
				break;
		case 3: System.out.println("Enter notification type");
				String one = reader.next().toLowerCase();
				NotificationTypes type;
				switch (one) {
				case "email": type = NotificationTypes.EMAIL;
								break;
				case "cellphone": type = NotificationTypes.CELLPHONE;
							break;
				case "pigeon_post": type = NotificationTypes.PIGEON_POST;
							break;
				default: type = NotificationTypes.EMAIL;
				}
				addNotification(student, type);
		case 4: printRecord(student);
				break;
		default: System.out.println("Try again");
				student(student);
				break;
		}
	}
	private void admin(LoggedInAdmin admin) {
		Scanner reader = new Scanner(System.in);
		System.out.println("1) start \n2) restart \n3) stop\n");
		int choice = reader.nextInt();
		switch (choice) {
		case 1: start(admin);
				break;
		case 2: restart(admin);
				break;
		case 3: stop(admin);
				break;
		default: System.out.println("Try again");
				admin(admin);
				break;
		}
	}
	public void select(LoggedInAuthenticatedUser user) {
		Scanner reader = new Scanner(System.in);
		switch (user.getType().toLowerCase()) {
		case "admin": admin((LoggedInAdmin) user);
					break;
		case "instructor": instructor((LoggedInInstructor) user);
						break;
		case "student" : student((LoggedInStudent) user);
					break;
		default: 	System.out.println("Def");
					break;
		}
		System.out.println("would you like to perform another operation? (y/n) ");
		String line = reader.next().toUpperCase();
		if (line.equalsIgnoreCase("Y"))
			select(user);
	}
}
