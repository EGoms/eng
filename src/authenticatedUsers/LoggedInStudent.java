package authenticatedUsers;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Scanner;

import authenticationServer.AuthenticationToken;
import customDatatypes.EvaluationTypes;
import customDatatypes.Marks;
import customDatatypes.NotificationTypes;
import offerings.ICourseOffering;
import registrar.ModelRegister;
import systemUsers.StudentModel;

public class LoggedInStudent implements LoggedInAuthenticatedUser {

	private String name;
	private String surname;
	private String ID;
	private AuthenticationToken authenticationToken;
	private String type = "Student";
	
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

	/**
	 * added so we can store the type
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * These are the same methods that are implemented in LoggedInStudentOperation. The difference is that these are called on the logged
	 * in student objects and don't require passing a LoggedInAuthenticatedUser as parameter
	 */
	
	public void enroll() {
		StudentModel target = verifyStudent(this.authenticationToken); //authenticate the student using this object's token
		if (target == null) {
			System.out.println("Student is not registered"); //if there is no student 
			return;
		}
		
		Scanner reader = new Scanner(System.in);					//set up a reader for input
		
		if (target.getCoursesEnrolled() == null) {				//if the target isn't enrolled in any courses make a new list of courses
			target.setCoursesEnrolled(new ArrayList<ICourseOffering>());
		}
		for (ICourseOffering icourse : target.getCoursesAllowed()) { //get the courses that the student is allowed to enroll in
			if (target.getCoursesEnrolled().contains(icourse))
				 return;
			System.out.println(target.getName() +" "+target.getSurname()); //print the name of the student doing the operation
			System.out.println("Do you wish to enroll in: " +  icourse.getCourseName() + "?");	//prompt if they want to enrll
			System.out.println("1 for yes, 0 for no");
			int line = reader.nextInt(); //get input
			if (line == 0) //if they don't want to enroll go forward, to next course, or done with enroll for the student
				continue;
			System.out.println("Select Enrollment status: \n1) FC \n2) FA \n3) PC \n4) PA, default is: " + target.getEvaluationEntities().get(icourse));
			line = reader.nextInt(); //read enrollment status
			EvaluationTypes type;
			switch (line) { //switch on the input to select the type as one of the enumerated values
				case 1: type = EvaluationTypes.FULL_CREDIT;
						break;
				case 2: type = EvaluationTypes.FULL_AUDIT;
						break;
				case 3: type = EvaluationTypes.PART_CREDIT;
						break;
				case 4: type = EvaluationTypes.PART_AUDIT;
						break;
				default: type = target.getEvaluationEntities().get(icourse);
						break;
			}
			if (!target.getCoursesEnrolled().contains(icourse)) { //after all that is done, if the student isn't already enrolled
				target.getEvaluationEntities().put(icourse, type); //add the course and the evaluation type to the student
				target.getCoursesEnrolled().add(icourse);		//add the course to the student's enrolled courses
				icourse.getStudentsEnrolled().add(target);		//add the student to the course's student
			}
		}
		addNotification(NotificationTypes.EMAIL);
	}
	
	public void selectNotification() {
		NotificationTypes type = NotificationTypes.PIGEON_POST; //default
		StudentModel target = verifyStudent(this.authenticationToken);
	
		if (target == null) { //
			System.out.println("Student does not exist");
			return;
		}
		System.out.println(target.getName()+ " "+target.getSurname());
		System.out.println("Select notification: \n1) Email \n2) Cellphone \n3) Pigeon Post");
		Scanner reader = new Scanner(System.in);
		int line = reader.nextInt();
		
		switch (line) { //get the correct notification type from the enumeration by user input, default is pigeon post
			case 1: type = NotificationTypes.EMAIL;
					break;
			case 2: type = NotificationTypes.CELLPHONE;
					break;
			case 3: type = NotificationTypes.PIGEON_POST;
					break;
			default: type = NotificationTypes.PIGEON_POST;
					break;
			}
		
		target.setNotificationType(type); //set the notification type for the student
		System.out.println("Your notification type is: " + target.getNotificationType());
	}
	/**
	 * this method changes the notification type for the student to the parameter
	 * @param notification
	 */
	public void addNotification(NotificationTypes notification) {
		StudentModel target = verifyStudent(this.authenticationToken);
		if (target == null) {
			System.out.println("User not in Register");
			return;
		}
//		System.out.println(target.getName() + " " + target.getSurname());
		target.setNotificationType(notification);
		
	}

	/**
	 * prints the final grade for every course the student is enrolled in
	 * can't verify this works until marks are added to course
	 * currently works when the student has marks for every assignment, change to maybe just print the current marks they have
	 */
	public void printRecord() {
		StudentModel target = verifyStudent(this.authenticationToken);
		if (target.getCoursesEnrolled() == null) {
			System.out.println("Student not enrolled in any courses");
			return;
		}
		for (ICourseOffering course : target.getCoursesEnrolled()) {
			if (target.getPerCourseMarks() == null || !target.getPerCourseMarks().containsKey(course)) {
				System.out.println("No marks for " + target.getName()+ " in "+course.getCourseName());
				System.out.println();
			} else {
				System.out.println("Grades for " + target.getName()+ " "+target.getSurname() + " in " + course.getCourseName());
				try {
					Marks marks = target.getPerCourseMarks().get(course);
					marks.initializeIterator();
					while (marks.hasNext()) {
						Entry<String, Double> current = marks.getNextEntry();
						System.out.println(current.getKey() + " " + current.getValue());
					}
					System.out.print("Final mark - ");
					course.calculateFinalGrade(this.getID());
					System.out.println();
				} catch (NullPointerException e) {
					System.out.println("Not all entities have marks");
					System.out.println();
				}
			}
		}
	}
	
	private StudentModel verifyStudent(AuthenticationToken token) {
		StudentModel student = null;
		if (token.getUserType().equalsIgnoreCase("Student")) {
			student = ModelRegister.getInstance().checkIfUserHasAlreadyBeenCreated(token.getTokenID()) ? (StudentModel) ModelRegister.getInstance().getRegisteredUser(token.getTokenID()) : null;
		}
		return student;
	}
}
