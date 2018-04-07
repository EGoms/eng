package operations;

import java.io.FileNotFoundException;
import java.io.IOException;

import authenticatedUsers.LoggedInAuthenticatedUser;

public interface ILoggedInAdminOperation {
	void start(LoggedInAuthenticatedUser user) throws IOException;
	void restart(LoggedInAuthenticatedUser user) throws IOException;
	void stop(LoggedInAuthenticatedUser user) throws IOException, FileNotFoundException;
}
