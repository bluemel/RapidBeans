package org.rapidbeans.security;

public class PwdPolicy {

	public String check(final String pwd) {
		String checkFailure = null;
		if (pwd == null || pwd.length() == 0) {
			checkFailure = "pwdchange.wrong.pwd.empty";
		} else if (pwd.length() < 8) {
			checkFailure = "pwdchange.wrong.pwd.tooshort";
		}
		return checkFailure;
	}
}
