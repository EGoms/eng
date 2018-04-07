package operations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import authenticatedUsers.LoggedInAuthenticatedUser;
import customDatatypes.EvaluationTypes;
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
		BufferedReader reader = new BufferedReader(new FileReader(new File("note_1.txt")));
		CourseOffering course =  factory.createCourseOffering(reader);
		reader.close();
		reader = new BufferedReader(new FileReader(new File("note_2.txt")));
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
		File dir = new File("./");
		for (File file : dir.listFiles()) {
			if (file.isFile() && (file.getName().contains("class_"))) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				CourseOffering course = fact.createCourseOffering(reader);
				reader.close();
				admin.getCourses().add(course);
			}
		}
		System.out.println(admin.getCourses());
				
	}

	@Override
	public void stop(LoggedInAuthenticatedUser user) throws IOException, FileNotFoundException {
		if (user.getAuthenticationToken().getUserType() != "Admin") {
			System.out.println("You are not an admin");
			return;
		}
		int i = 1;
		for (CourseOffering x : ModelRegister.getInstance().getAllCourses()) {
			//PrintWriter writer = new PrintWriter("class_"+ i+".txt", "UTF-8");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("class_" +i+".txt"))) {
				i++;
				String toWrite = x.getCourseName() + "\t" + x.getCourseID() + "\t" + x.getSemester() + "\n";
				writer.write(toWrite);
				int instructors = x.getInstructor().size();
				toWrite = "TUTOR" + "\t" + "LIST" + "\t" + instructors +"\n";
				writer.write(toWrite);
				
				for (InstructorModel y : x.getInstructor()) {
					toWrite = y.getName() + "\t" + y.getSurname() + "\t" + y.getID() + "\n";
					writer.write(toWrite);
				}
				
				int studentsAllowed = x.getStudentsAllowedToEnroll().size();
				int studentsEnrolled = x.getStudentsEnrolled().size();
				
				toWrite = "ELLIGIBLE" + "\t" + "STUDENTS" + "\t" + studentsAllowed + "\n";
				writer.write(toWrite);
				
				for (StudentModel y : x.getStudentsAllowedToEnroll()) {
					toWrite = y.getName() + "\t" + y.getSurname() + "\t" + y.getID() + "\t" + y.getEvaluationEntities().get(x).getText() + "\n";
					writer.write(toWrite);
				}
				
				toWrite = "EVALUATION" + "\t" + "ENTITIES" + "\n";
				writer.write(toWrite);
				
				for (EvaluationTypes y : x.getEvaluationStrategies().keySet()) {
					toWrite = y.getText() + "\n";
					writer.write(toWrite);
					List<String> lines = new ArrayList<String>();
					//int length = x.getEvaluationStrategies().size();
//					toWrite = "TOTAL" + "\t" + "ENTITIES:" + "\t" + length + "\n";
//					writer.write(toWrite);
					int length = 0;
					
					Weights assignments = x.getEvaluationStrategies().get(y);
					assignments.initializeIterator();
					while (assignments.hasNext()) {
						length++;
						Entry<String, Double> current = assignments.getNextEntry();
						toWrite = current.getKey() + "\t" + current.getValue() + "\n";
						lines.add(toWrite);
					}
					toWrite = "TOTAL" + "\t" + "ENTITIES:" + "\t" + length + "\n";
					writer.write(toWrite);
					
					for (String str : lines) {
						writer.write(str);
					}
					
					
				}
				for (StudentModel z : x.getStudentsEnrolled()) {
					toWrite = z.getName() + "\t" + z.getSurname() + "\t" + z.getID() + "\n";
					writer.write(toWrite);
				}
			} catch (IOException e) {
				e.printStackTrace();
				//writer.close();
			}
		
		}
		System.exit(0);
	}

}
