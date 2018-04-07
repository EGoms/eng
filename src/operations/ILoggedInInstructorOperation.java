package operations;

import authenticatedUsers.LoggedInAuthenticatedUser;

public interface ILoggedInInstructorOperation {
	void modifyMark(LoggedInAuthenticatedUser user);
	void calcGrade(LoggedInAuthenticatedUser user);
	void printRecord(LoggedInAuthenticatedUser user);
	void addMark(LoggedInAuthenticatedUser user);
	
}
