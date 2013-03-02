/*
 * Partially generated code file: User.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.security.User
 * 
 * model:    model/org/rapidbeans/security/User.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.security;

// BEGIN manual code section
// User.import
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidBean;

import sun.misc.BASE64Encoder;

// END manual code section

/**
 * Rapid Bean class: User. Partially generated Java class !!!Do only edit
 * manually in marked sections!!!
 **/
public class User extends RapidBeanImplStrict {
	// BEGIN manual code section
	// User.classBody
	/**
	 * Reset the pwd property to null for a generic bean.
	 */
	public static void resetPwd(final GenericBean bean) {
		bean.setPropValue("pwd", null);
	}

	/**
	 * Hash a pwd.
	 * 
	 * @param pwdIn
	 *            the pwd to hash
	 * @param pwdHashAlgorithm
	 *            the hash algorithm used
	 * 
	 * @return the hashed pwd
	 */
	public static final String hashPwd(final String pwdIn,
			final String pwdHashAlgorithm) {
		String pwdHashed = null;
		try {
			final MessageDigest md = MessageDigest
					.getInstance(pwdHashAlgorithm);
			md.update(pwdIn.getBytes());
			pwdHashed = (new BASE64Encoder()).encode(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RapidBeansRuntimeException(e);
		}
		return pwdHashed;
	}

	/**
	 * setter for Property 'pwd'.
	 * 
	 * @param argValue
	 *            value of Property 'pwd' to set
	 * @param pwdHashAlgorithm
	 *            specify the hash algorithm used or null if you do not want to
	 *            hash
	 */
	public final void setPwdSec(final String argValue,
			final String pwdHashAlgorithm) {
		if (argValue == null) {
			setPwd(null);
		} else {
			if (pwdHashAlgorithm == null) {
				setPwd(argValue);
			} else {
				setPwd(hashPwd(argValue, pwdHashAlgorithm));
			}
		}
	}

	/**
	 * setter for Property 'pwd'.
	 * 
	 * @param argValue
	 *            value of Property 'pwd' to set
	 * @param pwdHashAlgorithm
	 *            specify the hash algorithm used or null if you do not want to
	 *            hash
	 */
	public static final void setPwdSecS(final RapidBean user,
			final String argValue, final String pwdHashAlgorithm) {
		if (argValue == null) {
			user.setPropValue("pwd", null);
		} else {
			if (pwdHashAlgorithm == null) {
				user.setPropValue("pwd", argValue);
			} else {
				user.setPropValue("pwd", hashPwd(argValue, pwdHashAlgorithm));
			}
		}
	}

	/**
	 * determines if the user has the role with the given name.
	 * 
	 * @param rolename
	 *            the role's name
	 * 
	 * @return true if the user has the role, false otherwise
	 */
	public boolean hasRole(final String rolename) {
		final Collection<RapidEnum> roles = (Collection<RapidEnum>) this
				.getRoles();
		if (roles != null) {
			for (final RapidEnum role : roles) {
				if (role.name().equals(rolename)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Reset the pwd.
	 */
	public void resetPwd() {
		this.setPwd(null);
	}

	/**
	 * Generic role check. Works for concrete User instances as well as for
	 * generic ones.
	 * 
	 * @param authenticatedUser
	 *            the concrete or generic user instances
	 * @param string
	 *            the
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasRoleGeneric(final RapidBean user,
			final String rolename) {
		if (!TypeRapidBean.isSameOrSubtype(
				TypeRapidBean.forName("org.rapidbeans.security.User"),
				user.getType())) {
			throw new RapidBeansRuntimeException("Bean type \""
					+ user.getType().getName() + "\" is no user.");
		}
		if (user instanceof User) {
			return ((User) user).hasRole(rolename);
		} else if (user instanceof GenericBean) {
			final List<RapidEnum> userrolenames = (List<RapidEnum>) user
					.getPropValue("roles");
			for (final RapidEnum userRolename : userrolenames) {
				if (userRolename.name().equals(rolename)) {
					return true;
				}
			}
		}
		return false;
	}

	// END manual code section

	/**
	 * property "accountname".
	 */
	private org.rapidbeans.core.basic.PropertyString accountname;

	/**
	 * property "pwd".
	 */
	private org.rapidbeans.core.basic.PropertyString pwd;

	/**
	 * property "lastname".
	 */
	private org.rapidbeans.core.basic.PropertyString lastname;

	/**
	 * property "firstname".
	 */
	private org.rapidbeans.core.basic.PropertyString firstname;

	/**
	 * property "email".
	 */
	private org.rapidbeans.core.basic.PropertyString email;

	/**
	 * property "roles".
	 */
	private org.rapidbeans.core.basic.PropertyChoice roles;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.accountname = (org.rapidbeans.core.basic.PropertyString) this
				.getProperty("accountname");
		this.pwd = (org.rapidbeans.core.basic.PropertyString) this
				.getProperty("pwd");
		this.lastname = (org.rapidbeans.core.basic.PropertyString) this
				.getProperty("lastname");
		this.firstname = (org.rapidbeans.core.basic.PropertyString) this
				.getProperty("firstname");
		this.email = (org.rapidbeans.core.basic.PropertyString) this
				.getProperty("email");
		this.roles = (org.rapidbeans.core.basic.PropertyChoice) this
				.getProperty("roles");
	}

	/**
	 * default constructor.
	 */
	public User() {
		super();
		// BEGIN manual code section
		// User.User()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public User(final String s) {
		super(s);
		// BEGIN manual code section
		// User.User(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public User(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// User.User(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean
			.createInstance(User.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'accountname'
	 */
	public String getAccountname() {
		try {
			return (String) this.accountname.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"accountname");
		}
	}

	/**
	 * setter for Property 'accountname'.
	 * 
	 * @param argValue
	 *            value of Property 'accountname' to set
	 */
	public void setAccountname(final String argValue) {
		this.accountname.setValue(argValue);
	}

	/**
	 * @return value of Property 'pwd'
	 */
	public String getPwd() {
		try {
			return (String) this.pwd.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"pwd");
		}
	}

	/**
	 * setter for Property 'pwd'.
	 * 
	 * @param argValue
	 *            value of Property 'pwd' to set
	 */
	public void setPwd(final String argValue) {
		this.pwd.setValue(argValue);
	}

	/**
	 * @return value of Property 'lastname'
	 */
	public String getLastname() {
		try {
			return (String) this.lastname.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"lastname");
		}
	}

	/**
	 * setter for Property 'lastname'.
	 * 
	 * @param argValue
	 *            value of Property 'lastname' to set
	 */
	public void setLastname(final String argValue) {
		this.lastname.setValue(argValue);
	}

	/**
	 * @return value of Property 'firstname'
	 */
	public String getFirstname() {
		try {
			return (String) this.firstname.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"firstname");
		}
	}

	/**
	 * setter for Property 'firstname'.
	 * 
	 * @param argValue
	 *            value of Property 'firstname' to set
	 */
	public void setFirstname(final String argValue) {
		this.firstname.setValue(argValue);
	}

	/**
	 * @return value of Property 'email'
	 */
	public String getEmail() {
		try {
			return (String) this.email.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"email");
		}
	}

	/**
	 * setter for Property 'email'.
	 * 
	 * @param argValue
	 *            value of Property 'email' to set
	 */
	public void setEmail(final String argValue) {
		this.email.setValue(argValue);
	}

	/**
	 * @return value of Property 'roles'
	 */
	@SuppressWarnings("unchecked")
	public java.util.List<org.rapidbeans.core.basic.RapidEnum> getRoles() {
		try {
			return (java.util.List<org.rapidbeans.core.basic.RapidEnum>) this.roles
					.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"roles");
		}
	}

	/**
	 * setter for Property 'roles'.
	 * 
	 * @param argValue
	 *            value of Property 'roles' to set
	 */
	public void setRoles(
			final java.util.List<org.rapidbeans.core.basic.RapidEnum> argValue) {
		this.roles.setValue(argValue);
	}
}
