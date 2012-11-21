package ir.mohammadi.android.nightly.sqlite;

public class UserInfo {

	private String user_id = null;
	private String user_email = null;
	private String user_password = null;

	public String getUserId() {
		return user_id;
	}

	public String getUserEmail() {
		return user_email;
	}

	public String getUserPassword() {
		return user_password;
	}

	public void setUserId(String user_id) {
		this.user_id = user_id;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public void setUserPassword(String user_password) {
		this.user_password = user_password;
	}
}
