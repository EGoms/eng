package operations;

import java.io.IOException;

import authenticatedUsers.LoggedInAuthenticatedUser;

public interface ILoggedInAdminOperation {
	void start(LoggedInAuthenticatedUser user) throws IOException;
	void stop(LoggedInAuthenticatedUser user);
}
