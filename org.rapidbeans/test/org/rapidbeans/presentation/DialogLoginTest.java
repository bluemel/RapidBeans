/*
 * RapidBeans Framework: DialogLoginTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * 13.11.2007
 */
package org.rapidbeans.presentation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.util.FileHelper;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.config.ConfigApplication;
import org.rapidbeans.presentation.config.ConfigAuthorization;
import org.rapidbeans.security.User;
import org.rapidbeans.security.UserAuthnData;

/**
 * @author Martin Bl�mel
 */
public class DialogLoginTest {

	@After
	public void tearDown() {
		ApplicationManager.resetApplication();
		// reset testsettings.xml
		FileHelper.copyFile(new File("testdata/testsettings_backup.xml"),
				new File("testdata/testsettings.xml"), true);
	}

	/**
	 * Test login successfully.
	 */
	@Test
	public void testLoginSuccessfully() {
		final ApplicationMock client = new ApplicationMock(null);
		ApplicationManager.start(client);
		String[][] loginData = { { "herbert", "s2sh!4all" }, };
		LoginDialogMock dialogMock = new LoginDialogMock(loginData, true);
		RapidBean user = DialogLogin.login(1, dialogMock);
		Assert.assertEquals("herbert", user.getPropValue("accountname"));
		Assert.assertEquals(0, client.errorMessages.size());
		Assert.assertEquals(0, client.infoMessages.size());
		Assert.assertEquals(0, client.yesNoMessages.size());
	}

	/**
	 * Test login successfully.
	 */
	@Test
	public void testLoginSuccessfullyPwdHash() {
		final ApplicationMock client = new ApplicationMock("SHA-1");
		ApplicationManager.start(client);
		String[][] loginData = { { "herbert", "s2sh!4all" }, };
		LoginDialogMock dialogMock = new LoginDialogMock(loginData, true);
		RapidBean user = DialogLogin.login(1, dialogMock);
		Assert.assertEquals("herbert", user.getPropValue("accountname"));
		Assert.assertEquals(0, client.errorMessages.size());
		Assert.assertEquals(0, client.infoMessages.size());
		Assert.assertEquals(0, client.yesNoMessages.size());
	}

	/**
	 * Test login successfully.
	 */
	@Test
	public void testLoginSuccessfullyPwdHash512() {
		final ApplicationMock client = new ApplicationMock("SHA-512");
		ApplicationManager.start(client);
		String[][] loginData = { { "herbert", "s2sh!4all" }, };
		LoginDialogMock dialogMock = new LoginDialogMock(loginData, true);
		RapidBean user = DialogLogin.login(1, dialogMock);
		Assert.assertEquals("herbert", user.getPropValue("accountname"));
		Assert.assertEquals(0, client.errorMessages.size());
		Assert.assertEquals(0, client.infoMessages.size());
		Assert.assertEquals(0, client.yesNoMessages.size());
	}

	/**
	 * Test login successfully without password.
	 */
	@Test
	public void testLoginSuccessfullyWithoutPwd() {
		final ApplicationMock client = new ApplicationMock(null);
		ApplicationManager.start(client);
		String[][] loginData = { { "herbert", "" }, };
		LoginDialogMock dialogMock = new LoginDialogMock(loginData, true);
		User herbert = (User) client.getAuthnDoc().findBean(
				"org.rapidbeans.security.User", "herbert");
		herbert.setPwdSec(null, null);
		RapidBean user = DialogLogin.login(1, dialogMock);
		Assert.assertEquals("herbert", user.getPropValue("accountname"));
		Assert.assertEquals(null, user.getPropValue("pwd"));
		Assert.assertEquals(0, client.errorMessages.size());
		Assert.assertEquals(0, client.infoMessages.size());
		Assert.assertEquals(0, client.yesNoMessages.size());
	}

	/**
	 * Test login failed first time.
	 */
	@Test
	public void testLoginFailed1() {
		final ApplicationMock client = new ApplicationMock(null);
		ApplicationManager.start(client);
		String[][] loginData = { { "herbert", "xxx" }, };
		LoginDialogMock dialogMock = new LoginDialogMock(loginData, true);
		RapidBean user = DialogLogin.login(1, dialogMock);
		Assert.assertNull(user);
		Assert.assertEquals(1, client.errorMessages.size());
		Assert.assertEquals("login.failed.shut", client.errorMessages.get(0));
		Assert.assertEquals(0, client.infoMessages.size());
		Assert.assertEquals(0, client.yesNoMessages.size());
	}

	/**
	 * Test login failed after 5 times.
	 */
	@Test
	public void testLoginFailed5() {
		ApplicationMock client = new ApplicationMock(null);
		ApplicationManager.start(client);
		String[][] loginData = { { "herbert", "xxx" }, { "herbert", "xxx" },
				{ "herbert", "xxx" }, { "herbert", "xxx" },
				{ "herbert", "xxx" }, { "herbert", "s2sh!4all" } };
		LoginDialogMock dialogMock = new LoginDialogMock(loginData, true);
		RapidBean user = DialogLogin.login(5, dialogMock);
		Assert.assertNull(user);
		Assert.assertEquals(5, client.errorMessages.size());
		Assert.assertEquals("login.failed", client.errorMessages.get(0));
		Assert.assertEquals("login.failed", client.errorMessages.get(1));
		Assert.assertEquals("login.failed", client.errorMessages.get(2));
		Assert.assertEquals("login.failed.uno", client.errorMessages.get(3));
		Assert.assertEquals("login.failed.shut", client.errorMessages.get(4));
		Assert.assertEquals(0, client.infoMessages.size());
		Assert.assertEquals(0, client.yesNoMessages.size());
	}

	/**
	 * Test login success after 5 times.
	 */
	@Test
	public void testLoginSuccessWithLastOf5Tries() {
		ApplicationMock client = new ApplicationMock(null);
		ApplicationManager.start(client);
		String[][] loginData = { { "herbert", "xxx" }, { "herbert", "xxx" },
				{ "herbert", "xxx" }, { "herbert", "xxx" },
				{ "herbert", "s2sh!4all" }, };
		LoginDialogMock dialogMock = new LoginDialogMock(loginData, true);
		RapidBean user = DialogLogin.login(5, dialogMock);
		Assert.assertEquals("herbert", user.getPropValue("accountname"));
		Assert.assertEquals(4, client.errorMessages.size());
		Assert.assertEquals("login.failed", client.errorMessages.get(0));
		Assert.assertEquals("login.failed", client.errorMessages.get(1));
		Assert.assertEquals("login.failed", client.errorMessages.get(2));
		Assert.assertEquals("login.failed.uno", client.errorMessages.get(3));
		Assert.assertEquals(0, client.infoMessages.size());
		Assert.assertEquals(0, client.yesNoMessages.size());
	}

	private class ApplicationMock extends Application {

		private String pwdHashAlg = null;

		public ApplicationMock(final String pwdHalg) {
			this.pwdHashAlg = pwdHalg;
			this.setSettingsDoc(new Document(new File(
					"testdata/testsettings.xml")));
		}

		@Override
		public boolean getTestMode() {
			return true;
		}

		@Override
		public void init() {
			ConfigAuthorization configAuthn = new ConfigAuthorization();
			configAuthn.setPwdhashalgorithm(this.pwdHashAlg);
			this.configuration.setAuthorization(configAuthn);
			this.setCurrentLocale(new LocaleMock());
		}

		private ConfigApplication configuration = new ConfigApplication();

		@Override
		public ConfigApplication getConfiguration() {
			return this.configuration;
		}

		private Document authnDoc = null;

		@Override
		public Document getAuthnDoc() {
			if (this.authnDoc == null) {
				User user = new User("herbert");
				user.setPwdSec("s2sh!4all", pwdHashAlg);
				UserAuthnData authn = new UserAuthnData();
				authn.addUser(user);
				this.authnDoc = new Document("authn", authn);
			}
			return this.authnDoc;
		}

		public List<String> infoMessages = new ArrayList<String>();

		@Override
		public void messageInfo(final String message, final String title) {
			this.infoMessages.add(message);
		}

		public List<String> errorMessages = new ArrayList<String>();

		@Override
		public void messageError(final String message, final String title) {
			this.errorMessages.add(message);
		}

		public List<String> yesNoMessages = new ArrayList<String>();

		@Override
		public boolean messageYesNo(final String message, final String title) {
			this.yesNoMessages.add(message);
			return true;
		}
	}

	private class LoginDialogMock extends DialogLogin {

		private String[][] loginData = null;

		private int loginDataIndex = 0;

		private boolean ok;

		public LoginDialogMock(final String[][] ld, final boolean o) {
			this.loginData = ld;
			this.ok = o;
		}

		public boolean showLogin() {
			return this.ok;
		}

		public void dispose() {
		}

		// protected void setOk(boolean ok) {
		// this.ok = ok;
		// }

		protected String getLoginname() {
			return this.loginData[loginDataIndex][0];
		}

		protected void setLoginname(final String l) {
		}

		protected String getPwd() {
			return this.loginData[loginDataIndex++][1];
		}

		protected void setPwd(final String p) {
		}

		protected boolean getSavecred() {
			return false;
		}

		protected boolean getEncryptcred() {
			return false;
		}
	}

	private class LocaleMock extends RapidBeansLocale {

		@Override
		public String getStringGui(final String key) {
			return key;
		}

		@Override
		public String getStringMessage(final String key) {
			return key;
		}

		@Override
		public String getStringMessage(final String key, final String title) {
			return key;
		}
	}
}
