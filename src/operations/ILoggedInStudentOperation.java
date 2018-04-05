package operations;

import authenticatedUsers.LoggedInAuthenticatedUser;
import authenticatedUsers.LoggedInStudent;
import customDatatypes.NotificationTypes;
import offerings.CourseOffering;

public interface ILoggedInStudentOperation {
	void enroll(LoggedInAuthenticatedUser user);
	void selectNotification(LoggedInAuthenticatedUser user);
	void addNotification(LoggedInAuthenticatedUser user, NotificationTypes notification);
	void printRecord(LoggedInAuthenticatedUser user);
	
}
