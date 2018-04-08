package authenticationServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;

import java.util.ArrayList;

import java.util.List;

import java.util.Map;
import java.util.Scanner;

import authenticatedUsers.LoggedInAdmin;
import authenticatedUsers.LoggedInAuthenticatedUser;
import authenticatedUsers.LoggedInInstructor;
import authenticatedUsers.LoggedInStudent;
import loggedInUserFactory.LoggedInUserFactory;
import registrar.ModelRegister;

import registrar.ModelRegister;

/**
 * requires a file containing valid IDs and passwords
 * when attempting to log in you pass an ID and if the ID is in the file you get a token back which you pass to the logged in user factory
 * @author evangomolin
 * 
 */
public class LogInServer {

	private static LogInServer server;
	private static int onOff = 0;
	private static Map<String,String> logInInfo;
	private Map<String, LoggedInAuthenticatedUser> register;
	
	private LogInServer() {
		//TODO: Parse a file and save passwords/username in a structure for look up when logging in
		logInInfo = new HashMap<String, String>();
		logInInfo.put("0000", "pass");
		register = new HashMap<String, LoggedInAuthenticatedUser>();
		BufferedReader in;
		String line;
//		try {
//			in = new BufferedReader(new FileReader(new File("file.txt")));
//			while ((line = in.readLine()) != null) {
//				String id = line.split("\t")[0];
//				String pw = line.split("\t")[1];
//				logInInfo.put(id, pw);
//			}
//		} catch (IOException e) {
//			System.out.println(e.getMessage() + " exception thrown at LogInServer");
//			e.printStackTrace();
//		}
	}

	/**
	 * create a singleton log in server 
	 * @return
	 */
	public static LogInServer getServer() {
		if (server==null)
			server = new LogInServer();
		return server;
	}
	
	public Map<String, String> info() {
		return this.logInInfo;
	}

	/**
	 * can only log in if id is in logInInfo and id is not in the register (not already logged in)
	 * @param id
	 * @return true if id is in the map, false if not
	 */
	private boolean verify(String id, String pw) {
		if (logInInfo.containsKey(id)) {
			if (!register.containsKey(id) && logInInfo.get(id).equals(pw)) {
				return true;
			}
		}
		return false; 
	}
	/**
	 * takes a string id, calls the verify method which checks if its in the file of id/pw
	 * if the id is 0000 (admin) then we create a new token for the admin and turn onOff on
	 * then we can only log in other users if onOff = 1 (aka admin already logged in)
	 * @param id
	 * @return authenticationToken set with the user id and type
	 */
	public LoggedInAuthenticatedUser login() {
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter user ID: ");
		String line = reader.next();
		System.out.println("Enter password: ");
		String pw = reader.next();
		LoggedInUserFactory fact = new LoggedInUserFactory();
		if (!verify(line, pw)) {
			if (register.containsKey(line)) {
				return register.get(line);
			}
			System.out.println("User not registered");
			return login();
		}
		
		if (line.equals("0000")) {
			AuthenticationToken token = new AuthenticationToken();
			token.setTokenID(line);
			token.setUserType("Admin");
			onOff = 1;
			LoggedInAdmin admin = fact.createLoggedInAdmin(token);
			register.put(line, admin);
			//register.put(id, token);
			return admin;
		}
		if (onOff == 1) {
			AuthenticationToken token = new AuthenticationToken();
			token.setTokenID(line);
			token.setUserType(ModelRegister.getInstance().getRegisteredUser(line).getType());
			LoggedInAuthenticatedUser user = fact.createAuthenticatedUser(token);
			register.put(line, user);
			return user;
		} else {
			System.out.println("Admin must log in before users");
			return null;
		}
	}
	
	public LoggedInAdmin adminLogin() {
		LoggedInAdmin admin;
		LoggedInUserFactory factory = new LoggedInUserFactory();
		Scanner reader = new Scanner(System.in);
		System.out.print("Enter ID: ");
		String id = reader.next();
		System.out.print("Enter password: ");
		String pw = reader.next();
		
		if (!verify(id, pw)) {
			if (register.containsKey(id))
				return (LoggedInAdmin) register.get(id);
			else {
				System.out.println("Admin not registered");
				return null;
			}
		}
		
		if (id.equals("0000")) { //if admin
			AuthenticationToken token = new AuthenticationToken();
			token.setTokenID(id);
			token.setUserType("Admin");
			admin = (LoggedInAdmin) factory.createAuthenticatedUser(token);
			onOff = 1;
			register.put(id, admin);
			return admin;
		} else {
			return null;
		}
	}
	public void loginMany() {
		LoggedInAuthenticatedUser user;
		LoggedInUserFactory factory = new LoggedInUserFactory();
		Scanner reader = new Scanner(System.in);
		System.out.print("Enter ID: ");
		String id = reader.next();
		System.out.print("Enter password: ");
		String pw = reader.next();
		
		if (!verify(id, pw)) {
			if (register.containsKey(id)) {
				System.out.println("User already logged in");
				return;
			} else {
				System.out.println("User not registered");
				return;
			}
		}
		if (onOff == 1) { //other people are allowed to log in (admin is logged in)
			AuthenticationToken token = new AuthenticationToken();
			token.setTokenID(id);
			token.setUserType(ModelRegister.getInstance().getRegisteredUser(id).getType()); //extended attribute of admin/instructor/studentModel
			LoggedInAuthenticatedUser person = factory.createAuthenticatedUser(token);
			register.put(id, person);
			System.out.println("Would you like to log in another user? (y/n): ");
			String choice = reader.next().toUpperCase();
			if (choice.equals("Y")) //call again if we want to. all users get added to register
				loginMany();
		} else {
			System.out.println("Admin must log in first");
			return;
		}
		
	}
	public LoggedInAuthenticatedUser getLoggedInUser(String id) {
		return register.get(id);
	}
	
	public boolean checkIfUserIsLoggedIn(String id) {
		return register.containsKey(id);
	}
	
	public List<LoggedInAuthenticatedUser> getLoggedInUsers() {
		List<LoggedInAuthenticatedUser> users = new ArrayList<LoggedInAuthenticatedUser>();
		users.addAll(register.values());
		return users;
	}
	
	public List<LoggedInStudent> getLoggedInStudents() {
		List<LoggedInAuthenticatedUser> all = getLoggedInUsers();
		List<LoggedInStudent> students = new ArrayList<LoggedInStudent>();
		for (LoggedInAuthenticatedUser student : all) {
			if (student.getAuthenticationToken().getUserType().equals("Student")) {
				students.add((LoggedInStudent) student);
			}
		}
		return students;
	}
	public List<LoggedInInstructor> getLoggedInInstructors() {
		List<LoggedInAuthenticatedUser> all = getLoggedInUsers();
		List<LoggedInInstructor> instructors = new ArrayList<LoggedInInstructor>();
		for (LoggedInAuthenticatedUser instructor : all) {
			if (instructor.getAuthenticationToken().getUserType().equals("Instructor")) {
				instructors.add((LoggedInInstructor) instructor);
			}
		}
		return instructors;
	}
}
