package offerings;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import authenticationServer.LogInServer;
import customDatatypes.EvaluationTypes;
import customDatatypes.Marks;
import customDatatypes.Weights;
import registrar.ModelRegister;
import systemUserModelFactories.AdminModelFactory;
import systemUserModelFactories.InstructorModelFactory;
import systemUserModelFactories.StudentModelFactory;
import systemUserModelFactories.SystemUserModelFactory;
import systemUsers.InstructorModel;
import systemUsers.StudentModel;
import systemUsers.AdminModel;

public class OfferingFactory {

//	Instantiate 
	public CourseOffering createCourseOffering(BufferedReader br) {
		// TODO Auto-generated method stub
		try{
		String line = br.readLine();
		CourseOffering course = new CourseOffering();
//		Using the ModelRegister - its a utility class that allows us to keep track of which IDs have already been populated
//		if we don't have it registered
		if(!ModelRegister.getInstance().checkIfCourseHasAlreadyBeenCreated(line.split("\t")[1])){
//			we populate the course object and initialize all fields
			course.setCourseName(line.split("\t")[0]);
			course.setCourseID(line.split("\t")[1]);
			course.setSemester(Integer.parseInt(line.split("\t")[2]));
			course.setInstructor(new ArrayList<InstructorModel>());
			course.setStudentsAllowedToEnroll(new ArrayList<StudentModel>());
			course.setEvaluationStrategies(new HashMap<EvaluationTypes, Weights>());
			course.setStudentsEnrolled(new ArrayList<StudentModel>());
//			and add the new course to the register
			ModelRegister.getInstance().registerCourse(course.getCourseID(), course);
		}
//			otherwise we load the existing instance from the register
			course = ModelRegister.getInstance().getRegisteredCourse(line.split("\t")[1]);
			line = br.readLine();
//			We create an instance of an InstructorModelFactory to create InstructorModel instances
			SystemUserModelFactory theFactory = new InstructorModelFactory();
			for (int i=0;i<Integer.parseInt(line.split("\t")[2]);i++) {
//				the actual create instance method call
				course.getInstructor().add((InstructorModel)theFactory.createSystemUserModel(br, course));
			}
			line = br.readLine();
//			create an instance of StudentModelFactory and allocate it to the theFactory variable (same interface) to create StudentModel instances 
			theFactory = new StudentModelFactory();
			for (int i=0;i<Integer.parseInt(line.split("\t")[2]);i++) {
//				the actual create instance method call
				course.getStudentsAllowedToEnroll().add((StudentModel)theFactory.createSystemUserModel(br, course));
			}
//			consuming EVALUATION ENTITIES tag (you'll notice there are other standalone br.readLine() calls 
//			that consume and generally advance the input to the desirable line
			line = br.readLine();
			for(int i=0;i<4;i++) {
				line = br.readLine();
//				Check how this works || we select he appropriate enum value
				EvaluationTypes evaluationTypes = EvaluationTypes.fromString(line);
//				weights is an object we created for encapsulating the bare minimum of Map functionality that you'll probably need
				Weights weights = new Weights();
				line = br.readLine();
				int numberOfEvaluationWeights = Integer.parseInt(line.split("\t")[2]);
//				Read all the evaluation strategies that are available for a particular evaluation type and
				for(int j=0; j<numberOfEvaluationWeights;j++){
					line = br.readLine().toUpperCase();
					weights.addToEvalStrategy(line.split("\t")[0], Double.parseDouble(line.split("\t")[1]));
				}
//				add them to the course
				course.getEvaluationStrategies().put(evaluationTypes, weights);
			}
			
			//create an admin, uses a different method because file might not contain a line for admin, so we use defaults
//			AdminModelFactory fac = new AdminModelFactory();
//			if (!ModelRegister.getInstance().checkIfUserHasAlreadyBeenCreated("0000")) {
//				AdminModel admin = (AdminModel) fac.createAdmin(course);
//			}
//			AdminModel admin = (AdminModel) ModelRegister.getInstance().getRegisteredUser("0000");
//			admin.getCourses().add(course);
			return course;
			
		}catch(IOException ioe){
			System.out.println(ioe.getMessage() + "exception thrown at CourseOfferingCreation"); 
			return null;
		}
	}
	
	public void reenroll(BufferedReader file) {
		try {
			String line = file.readLine();
			while (line != null) {
				CourseOffering course = ModelRegister.getInstance().getRegisteredCourse(line.split("\t")[1]);
				String number = file.readLine();
				for (int i = 0; i < Integer.parseInt(number); i++) {
					line = file.readLine();
					StudentModel student = (StudentModel) ModelRegister.getInstance().getRegisteredUser(line.split("\t")[2]);
					course.getStudentsEnrolled().add(student);
					if (student.getCoursesEnrolled() == null) {
						List<ICourseOffering> allowed = new ArrayList<ICourseOffering>();
						allowed.add(course);
						student.setCoursesEnrolled(allowed);
					} else {
						student.getCoursesEnrolled().add(course);
					}
					if (student.getPerCourseMarks() == null) {
						Map<ICourseOffering, Marks> grades = new HashMap<ICourseOffering, Marks>();
						student.setPerCourseMarks(grades);
					}
					Marks marks = new Marks();
					student.getPerCourseMarks().put(course, marks);
					String numGrades = file.readLine();
					for (int j = 0; j < Integer.parseInt(numGrades); j++) {
						line = file.readLine();
						if (line == null)
							return;
						student.getPerCourseMarks().get(course).addToEvalStrategy(line.split("\t")[0], Double.parseDouble(line.split("\t")[1]));
					}
				}
				line = file.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
