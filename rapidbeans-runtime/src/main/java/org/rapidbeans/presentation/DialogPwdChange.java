/*
 * Rapid Beans Framework: DialogPwdChange.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/20/2007
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copies of the GNU Lesser General Public License and the
 * GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package org.rapidbeans.presentation;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.presentation.config.ApplicationGuiType;
import org.rapidbeans.presentation.swing.DialogPwdChangeSwing;
import org.rapidbeans.presentation.swing.EditorPropertyPwd;
import org.rapidbeans.security.PwdPolicy;
import org.rapidbeans.security.User;

/**
 * The super class that abstracts from gui implementation.
 * 
 * @author Martin Bluemel
 */
public abstract class DialogPwdChange {

	/**
	 * Converts the pwd entered into a string and erases the pwd input field.
	 * 
	 * @return the pwd entered
	 */
	protected abstract String getPwdOld();

	/**
	 * Converts the pwd entered into a string and erases the pwd input field.
	 * 
	 * @return the pwd entered
	 */
	protected abstract String getPwdNew1();

	/**
	 * Converts the pwd entered into a string and erases the pwd input field.
	 * 
	 * @return the pwd entered
	 */
	protected abstract String getPwdNew2();

	/**
	 * the dialog's title
	 */
	private String title;

	/**
	 * @return the title
	 */
	protected String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	protected void setTitle(String title) {
		this.title = title;
	}

	/**
	 * the user logged in.
	 */
	private RapidBean user = null;

	public static boolean start(final EditorPropertyPwd editor) {
		final RapidBean user = editor.getProperty().getBean();
		return start(editor, user);
	}

	public static boolean start(final RapidBean user) {
		return start(null, user);
	}

	private static boolean start(final EditorPropertyPwd editor, final RapidBean user) {
		final DialogPwdChange dialog = createDialog(user);
		final Application client = ApplicationManager.getApplication();
		final RapidBeansLocale loc = client.getCurrentLocale();
		boolean dialogCancelled = false;
		boolean pwdChanged = false;
		while ((!dialogCancelled) && (!pwdChanged)) {
			dialogCancelled = (!dialog.show());
			if (!dialogCancelled) {
				final String pwdOldUserHashed = (String) user.getPropValue("pwd");
				final String pwdOldEntered = dialog.getPwdOld();
				String pwdOldEnteredHashed = "";
				if (isGiven(pwdOldEntered)) {
					pwdOldEnteredHashed = User.hashPwd(pwdOldEntered,
							client.getConfiguration().getAuthorization().getPwdhashalgorithm());
				}
				if ((isEmpty(pwdOldUserHashed) && isGiven(pwdOldEnteredHashed))
						|| (isGiven(pwdOldUserHashed) && isEmpty(pwdOldEnteredHashed)) || (isGiven(pwdOldUserHashed)
								&& isGiven(pwdOldEnteredHashed) && (!pwdOldEnteredHashed.equals(pwdOldUserHashed)))) {
					client.messageError(loc.getStringMessage("pwdchange.wrong.pwd.old"),
							loc.getStringMessage("pwdchange.wrong.title"));
				} else {
					final String pwdNew1 = dialog.getPwdNew1();
					final String pwdNew2 = dialog.getPwdNew2();
					if (!(pwdNew1.equals(pwdNew2))) {
						client.messageError(loc.getStringMessage("pwdchange.wrong.pwd.new"),
								loc.getStringMessage("pwdchange.wrong.title"));
					} else {
						final String pwdPolicyCheckResult = new PwdPolicy().check(pwdNew1);
						if (pwdPolicyCheckResult != null) {
							client.messageError(loc.getStringMessage(pwdPolicyCheckResult),
									loc.getStringMessage("pwdchange.wrong.title"));
						} else {
							try {
								// prevent the editor from resetting the backup bean
								// when receiving the BeanChangedEvent
								if (editor != null) {
									editor.getBeanEditor().setModifies(true);
								}
								if (pwdNew1.equals("")) {
									User.setPwdSecS(user, null, null);
								} else {
									User.setPwdSecS(user, pwdNew1,
											client.getConfiguration().getAuthorization().getPwdhashalgorithm());
								}
							} finally {
								if (editor != null) {
									editor.getBeanEditor().setModifies(false);
								}
							}
							pwdChanged = true;
						}
					}
				}
			}
		}
		dialog.dispose();
		return pwdChanged;
	}

	private static boolean isEmpty(final String s) {
		return s == null || s.length() == 0;
	}

	private static boolean isGiven(final String s) {
		return s != null && s.length() > 0;
	}

	/**
	 * factory method to create the login dialog appropriate for the GUI toolkit
	 * chosen.
	 * 
	 * @return the login dialog created
	 */
	private static DialogPwdChange createDialog(final RapidBean user) {
		DialogPwdChange dialog = null;
		final Application client = ApplicationManager.getApplication();
		final ApplicationGuiType guitype = client.getConfiguration().getGuitype();
		switch (guitype) {
		case swing:
			dialog = new DialogPwdChangeSwing(user);
			break;
		default:
			throw new RapidBeansRuntimeException("gui type \"" + guitype.name() + "\" not supported");
		}
		return dialog;
	}

	/**
	 * this GUI toolkit specific method pops up a password change dialog.
	 * 
	 * @return if the dialog has been finished with OK (true) or Cancel (false),
	 *         Closing the dialog is interpreted as Cancel.
	 */
	protected abstract boolean show();

	/**
	 * @return the user
	 */
	protected RapidBean getUser() {
		return this.user;
	}

	/**
	 * @param usr the new user to set
	 */
	protected void setUser(final RapidBean usr) {
		this.user = usr;
	}

	/**
	 * Dispose the dialog widget.
	 */
	protected abstract void dispose();
}
