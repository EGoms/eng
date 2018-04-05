package authenticationServer;
/**
 * changed tokenID to type string so the value can be passed from the log in server through the authentication token to the logged
 * in user factory so the logged in user can have the appropriate id
 * @author evangomolin
 *
 */
public class AuthenticationToken {

	private String userType;
	private String tokenID;
	private Integer SessionID;

	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getTokenID() {
		return tokenID;
	}
	public void setTokenID(String id) {
		this.tokenID = id;
	}
	public Integer getSessionID() {
		return SessionID;
	}
	public void setSessionID(Integer sessionID) {
		SessionID = sessionID;
	}
	
}
