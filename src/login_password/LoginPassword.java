package login_password;

public class LoginPassword {
	public final String un, pw;

	private LoginPassword(final String un, final String pw) {
		this.un = un;
		this.pw = pw;
	}

	public static LoginPassword getLogin() {
		final DialogFrame unFrame = new DialogFrame("Username:", false);
		final DialogFrame pwFrame = new DialogFrame("Password:", true);

		final String un = unFrame.getAnswer();
		final String pw = pwFrame.getAnswer();

		return new LoginPassword(un, pw);
	}

	public static void main(String... args) {
		final LoginPassword lp = LoginPassword.getLogin();

		System.out.println("un: " + lp.un);
		System.out.println("pw: " + lp.pw);

		System.exit(0);
	}
}
