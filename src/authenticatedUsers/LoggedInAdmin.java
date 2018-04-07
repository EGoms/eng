package authenticatedUsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import authenticationServer.AuthenticationToken;
import customDatatypes.EvaluationTypes;
import customDatatypes.Marks;
import customDatatypes.Weights;
import offerings.CourseOffering;
import offerings.ICourseOffering;
import offerings.OfferingFactory;
import registrar.ModelRegister;
import systemUserModelFactories.AdminModelFactory;
import systemUsers.AdminModel;
import systemUsers.InstructorModel;
import systemUsers.StudentModel;

public class LoggedInAdmin implements LoggedInAuthenticatedUser {

	private String name;
	private String surname;
	private String ID;
	private AuthenticationToken authenticationToken;
	private String type = "Admin";
	
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
		return this.type;
	}
	
	/**
	 * normal start method, basically a copy of the test harness that loads the 2 course files and sets up the course objects
	 * as well as Student and Instructor Models, prints everything for a sanity check *** remove?
	 * @throws IOException
	 */
	public void start() throws IOException {
		if (!this.getType().equals("Admin")) {
			System.out.println("You are not an admin, how did you get here?");
			return;
		}
		AdminModelFactory adminFactory = new AdminModelFactory();
		AdminModel admin = adminFactory.createAdmin();
		OfferingFactory factory = new OfferingFactory();
		BufferedReader reader = new BufferedReader(new FileReader(new File("note_1.txt")));
		CourseOffering course =  factory.createCourseOffering(reader);
		reader.close();
		reader = new BufferedReader(new FileReader(new File("note_2.txt")));
		course = factory.createCourseOffering(reader);
		reader.close();
		
		//add courses to the admin's list
		for (CourseOffering icourse : ModelRegister.getInstance().getAllCourses())
			admin.getCourses().add(icourse);
		
		System.out.println(admin.getCourses());
		
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
	/**
	 * uses the files we generate from admin close operation to start the system
	 * @throws IOException
	 */
	public void restart() throws IOException {
		AdminModelFactory adminF = new AdminModelFactory();
		AdminModel admin = adminF.createAdmin();
		OfferingFactory fact = new OfferingFactory();
		File dir = new File("./");
		for (File file : dir.listFiles()) {
			if (file.isFile() && (file.getName().contains("note_"))) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				CourseOffering course = fact.createCourseOffering(reader);
				reader.close();
				admin.getCourses().add(course);
			}
		}
		System.out.println(admin.getCourses());
		for (File file : dir.listFiles()) {
			if (file.isFile() && (file.getName().contains("class_"))) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				fact.reenroll(reader);
				reader.close();
			}
		}
				
	}
	/**
	 * this saves each course in the system to a separate file containing all the information that was read in plus students
	 * enrolled and their grades
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void stop() {
		int i = 1;
		for (CourseOffering course : ModelRegister.getInstance().getAllCourses()) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("class_"+i+".txt"))) {
				i++;
				String toWrite = course.getCourseName() + "\t" + course.getCourseID() + "\n";
				writer.write(toWrite);
				int length = course.getStudentsEnrolled().size();
				writer.write(String.valueOf(length));
				writer.write("\n");
				for (int j = 0; j < length; j++) {
					StudentModel student = course.getStudentsEnrolled().get(j);
					toWrite = student.getName() + "\t" + student.getSurname() + "\t" + student.getID() + "\n";
					writer.write(toWrite);
					try {
						String grades = "";
						int numGrades = 0; 
						Marks marks = student.getPerCourseMarks().get(course);
						marks.initializeIterator();
						while (marks.hasNext()) {
							Entry<String, Double> next = marks.getNextEntry();
							grades += next.getKey() + "\t" + next.getValue() + "\n";
							numGrades++;
						}
						writer.write(numGrades);
						writer.write(grades);
					} catch (NullPointerException e) {
						writer.write(0);
						continue;
					}
				}
			} catch (IOException e) {
				
			}
		}
		System.exit(0);
	}
}
