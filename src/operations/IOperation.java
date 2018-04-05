package operations;

import authenticatedUsers.LoggedInAuthenticatedUser;

public interface IOperation {
	void operate(LoggedInAuthenticatedUser user);
}
