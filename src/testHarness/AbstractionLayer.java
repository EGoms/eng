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
	
	public void enroll(LoggedInStudent student) {
		student.enroll();
	}
	
	public void addNotification(NotificationTypes type) {
		for (LoggedInStudent student : LogInServer.getServer().getLoggedInStudents())
			student.addNotification(type);
	}
	
	public void addNotification(LoggedInStudent student, NotificationTypes type) {
		student.addNotification(type);
	}
	
	public void selectNotification() {
		for (LoggedInStudent student : LogInServer.getServer().getLoggedInStudents())
			student.selectNotification();
	}
	
	public void printRecord() {
		for (LoggedInStudent student : LogInServer.getServer().getLoggedInStudents())
			student.printRecord();
	}
	
	public void printRecord(LoggedInStudent student) {
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
	
	public void addMark(LoggedInInstructor instructor) {
		instructor.addMark();
	}
	public void modifyMark(LoggedInInstructor instructor) {
		instructor.modifyMark();
	}
	public void calcGrade(LoggedInInstructor instructor) {
		instructor.calcGrade();
	}
	public void printClass(LoggedInInstructor instructor) {
		instructor.printRecord();
	}
}
