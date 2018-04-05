package testHarness;

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
//import systemUsers.InstructorModel;
import operations.LoggedInStudentOperation;

public class TestStudentModelFactory_1 {
	
	public static void main(String[] args) throws IOException {
		/**
		LogInServer logServer = LogInServer.getServer();
		AuthenticationToken adminToken = logServer.login("0000");
		LoggedInAdminOperation adminOp = new LoggedInAdminOperation();
		//LoggedInUserFactory fact = new LoggedInUserFactory();
		//LoggedInAdmin admin = (LoggedInAdmin) fact.createAuthenticatedUser(adminToken);
		LoggedInAdmin admin = (LoggedInAdmin) LogInServer.getServer().getLoggedInUser("0000");
		adminOp.start(admin);
		
		AuthenticationToken instructorToken = logServer.login("1234");
		AuthenticationToken studentToken = logServer.login("6821");
		
		LoggedInStudentOperation studentOp = new LoggedInStudentOperation();
		LoggedInInstructorOperation instructorOp = new LoggedInInstructorOperation();
		
		//LoggedInInstructor instructor = fact.createLoggedInInstructor(instructorToken);
		LoggedInInstructor instructor = (LoggedInInstructor) LogInServer.getServer().getLoggedInUser("1234");
		LoggedInStudent student = (LoggedInStudent) LogInServer.getServer().getLoggedInUser("6821");
		
		//LoggedInStudent student = fact.createLoggedInStudent(studentToken);
		
		
		studentOp.enroll(student);
		instructorOp.addMark(instructor);
		StudentModel version = (StudentModel) ModelRegister.getInstance().getRegisteredUser(student.getID());
		//System.out.println(version.getEvaluationEntities());
		studentOp.printRecord(student);
		//System.out.println(admin.getID());
		AdminModel adminVersion = (AdminModel) ModelRegister.getInstance().getRegisteredUser(admin.getID());
		//System.out.println(adminVersion.getCourses());
		//System.out.println(adminVersion.getName() + adminVersion.getSurname());
		
	}
	*/
		LogInServer server = LogInServer.getServer();
		LoggedInAdmin admin = server.adminLogin();
		
		LoggedInAdminOperation adminOp = new LoggedInAdminOperation();
		LoggedInStudentOperation studentOp = new LoggedInStudentOperation();
		LoggedInInstructorOperation instructorOp = new LoggedInInstructorOperation();
		
		adminOp.start(admin);
		
		server.login();
		List<LoggedInAuthenticatedUser> loggedIn = server.getLoggedInUsers();
		List<LoggedInStudent> students = server.getLoggedInStudents();
		List<LoggedInInstructor> instructors = server.getLoggedInInstructors();
		System.out.println(loggedIn);
		
		for (LoggedInStudent stud : students) { //this one the method is called from the loggedInStudent class
			stud.enroll();
		}
		for (LoggedInInstructor inst : instructors)
			inst.addMark();
		//for (LoggedInStudent stud : students) {
		//	studentOp.enroll(stud);
		//}
		
		for (LoggedInStudent stud : students)
			stud.printRecord();
		
		//for all students in logged in users call enroll
//		for (LoggedInAuthenticatedUser student : loggedIn) {
//			if (student.getAuthenticationToken().getUserType().equals("Student")) {
//				studentOp.enroll(student);
//				studentOp.printRecord(student);
//			}
//		}
		
//		for (LoggedInAuthenticatedUser instructor : loggedIn) {
//			if (instructor.getAuthenticationToken().getUserType().equals("Instructor"))
//				instructorOp.calcGrade(instructor);
//		}
	}
}
/**
	public static void main(String[] args) throws IOException{
//		Create an instance of an OfferingFactory
		OfferingFactory factory = new OfferingFactory();
		BufferedReader br = new BufferedReader(new FileReader(new File("note_1.txt")));
//		Use the factory to populate as many instances of courses as many files we've got.
		CourseOffering	courseOffering = factory.createCourseOffering(br);
		br.close();
//		Loading 1 file at a time
		br = new BufferedReader(new FileReader(new File("note_2.txt")));
//		here we have only two files
		courseOffering = factory.createCourseOffering(br);
		br.close();
//		code to perform sanity checking of all our code
//		by printing all of the data that we've loaded
		for(CourseOffering course : ModelRegister.getInstance().getAllCourses()){
			System.out.println("ID : " + course.getCourseID() + "\nCourse name : " + course.getCourseName() + "\nSemester : " + 
			course.getSemester());
			System.out.println("Students allowed to enroll\n");
			for(StudentModel student : course.getStudentsAllowedToEnroll()){
				System.out.println("Student name : " + student.getName() + "\nStudent surname : " + student.getSurname() + 
						"\nStudent ID : " + student.getID() + "\nStudent EvaluationType : " + 
						student.getEvaluationEntities().get(course) + "\n\n");
			}
			
			for(StudentModel student : course.getStudentsAllowedToEnroll()){
				for(ICourseOffering course2 : student.getCoursesAllowed())
				System.out.println(student.getName() + "\t\t -> " + course2.getCourseName());
			}
			//for(InstructorModel instructor : course.getInstructor())
			//	System.out.println(instructor.getName() + "\t\t -> " + instructor.getIsTutorOf());
		}
		
<<<<<<< HEAD
		
		LogInServer login = LogInServer.getServer();
		AuthenticationToken token = login.logIn("3456");
		//System.out.println(token.getTokenID());
		LoggedInUserFactory fac = new LoggedInUserFactory();
		LoggedInStudent user = (LoggedInStudent) fac.createAuthenticatedUser(token);
		//System.out.println(user.getID());
		LoggedInStudentOperation op = new LoggedInStudentOperation();
		for (CourseOffering course : ModelRegister.getInstance().getAllCourses()) {
			op.enroll(user, course);
		}
		
		
		
=======
		LogInServer log = LogInServer.getServer();
		AuthenticationToken token = log.logIn("0000");
		AuthenticationToken tok = log.logIn("3456");
		tok = log.logIn("3456");
		LoggedInUserFactory fac = new LoggedInUserFactory();
		LoggedInStudent student = (LoggedInStudent) fac.createAuthenticatedUser(tok);
		LoggedInStudentOperation studentOps = new LoggedInStudentOperation();
		studentOps.addNotification(student, NotificationTypes.EMAIL);
		studentOps.selectNotification(student);
		studentOps.enroll(student);
//		for (CourseOffering course : ModelRegister.getInstance().getAllCourses()) {
//			studentOps.enroll(student, course);
//		}
		studentOps.printRecord(student);
>>>>>>> 4cb940fd3d6ff55a09a63a4a05fb3972aad7172b
	}
}
*/
