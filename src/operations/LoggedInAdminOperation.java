package operations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import authenticatedUsers.LoggedInAuthenticatedUser;
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

public class LoggedInAdminOperation implements ILoggedInAdminOperation {

	@Override
	public void start(LoggedInAuthenticatedUser user) throws IOException {
		if (user.getAuthenticationToken().getUserType() != "Admin") {
			System.out.println("You are not an admin");
			return;
		}
		AdminModelFactory adminFactory = new AdminModelFactory();
		AdminModel admin = adminFactory.createAdmin();
		OfferingFactory factory = new OfferingFactory();
		BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("note_1.txt")));
		CourseOffering course =  factory.createCourseOffering(reader);
		reader.close();
		reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("note_2.txt")));
		course = factory.createCourseOffering(reader);
		reader.close();
		
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
	
	public void restart(LoggedInAuthenticatedUser user) throws IOException {
		if (user.getAuthenticationToken().getUserType() != "Admin") {
			System.out.println("You are not an admin");
			return;
		}
		AdminModelFactory adminF = new AdminModelFactory();
		AdminModel admin = adminF.createAdmin();
		OfferingFactory fact = new OfferingFactory();
		File dir = new File("./src/");
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

	@Override
	public void stop(LoggedInAuthenticatedUser user) {
		if (user.getAuthenticationToken().getUserType() != "Admin") {
			System.out.println("You are not an admin");
			return;
		}
		int i = 1;
		for (CourseOffering course : ModelRegister.getInstance().getAllCourses()) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/class_"+i+".txt"))) {
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
							numGrades += 1;
						}
						writer.write(String.valueOf(numGrades));
						writer.write("\n");
						writer.write(grades);
					} catch (NullPointerException e) {
						writer.write("0");
						writer.write("\n");
						continue;
					}
				}
			} catch (IOException e) {
				
			}
		}
		System.exit(0);
	}

}
