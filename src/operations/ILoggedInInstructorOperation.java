package operations;

import authenticatedUsers.LoggedInAuthenticatedUser;

public interface ILoggedInInstructorOperation {
	void addMark(LoggedInAuthenticatedUser user, LoggedInAuthenticatedUser student);
	void modifyMark(LoggedInAuthenticatedUser user);
	void calcGrade(LoggedInAuthenticatedUser user);
	void printRecord(LoggedInAuthenticatedUser user);
	void printIndividualRecord(LoggedInAuthenticatedUser user);
	void printMultiple(LoggedInAuthenticatedUser user);
	void addMark(LoggedInAuthenticatedUser user);
	
}
