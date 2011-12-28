/*
 * Rapid Beans Framework: DialogLogin.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 11/08/2007
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

import java.util.logging.Logger;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.presentation.config.ApplicationGuiType;
import org.rapidbeans.presentation.settings.SettingsAll;
import org.rapidbeans.presentation.settings.SettingsAuthn;
import org.rapidbeans.presentation.settings.SettingsCred;
import org.rapidbeans.presentation.swing.DialogLoginSwing;
import org.rapidbeans.security.CryptoHelper;
import org.rapidbeans.security.LoginDialogResult;
import org.rapidbeans.security.User;

/**
 * The super class that abstracts from gui implementation.
 *
 * @author Martin Bluemel
 */
public abstract class DialogLogin {

    private static final Logger log = Logger.getLogger(
            DialogLogin.class.getName()); 

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

    /**
     * the gui type independent login method.
     *
     * @param maxtries maximal count of tries
     *
     * @return the authenticated user.
     */
    public static RapidBean login(final int maxtries) {
        return login(maxtries, createDialog());
    }

    /**
     * the gui type independent login method.
     *
     * @param maxtries maximal count of tries
     *
     * @return the authenticated user.
     */
    protected static RapidBean login(final int maxtries,
            final DialogLogin dialog) {
        final Application client = ApplicationManager.getApplication();
        final SettingsAll settings = client.getSettings();
        SettingsAuthn settingsAuthn = null;
        if (settings != null) {
            settingsAuthn = settings.getAuthn();
        }
        LoginDialogResult result = null;
        for (int tryno = maxtries; tryno > 0; tryno--) {
            result = dialog.tryLogin(tryno);
            if (result == LoginDialogResult.ok
                    || result == LoginDialogResult.cancelled
                    || (result == LoginDialogResult.nopwd
                            && dialog.user.getPropValue("pwd") == null)) {
                break;
            }
        }
        dialog.dispose();
        if (result == LoginDialogResult.ok
                || (result == LoginDialogResult.nopwd
                    && dialog.user.getPropValue("pwd") == null)) {
            if (settings != null) {
                if (settingsAuthn == null) {
                    settingsAuthn = new SettingsAuthn();
                    settings.setAuthn(settingsAuthn);
                }
                if (dialog.getSavecred()) {
                    SettingsCred cred = settingsAuthn.getCred();
                    if (cred == null) {
                        cred = new SettingsCred();
                    }
                    cred.setLogname(getTostoreLogname(dialog));
                    final String pwd = getTostorePwd(dialog);
                    cred.setPwd(pwd);
                    if (dialog.getEncryptcred()) {
                        cred.setEncrypted(true);
                    } else {
                        cred.setEncrypted(false);
                    }
                    settingsAuthn.setCred(cred);
                } else {
                    settingsAuthn.setCred(null);
                }
                client.getSettingsDoc().save();
            }
            return dialog.user;
        } else {
            return null;
        }
    }

    private static String getStoredLogname(final SettingsAuthn settingsAuthn) {
        String logname = settingsAuthn.getCred().getLogname();
        if (settingsAuthn.getCred().getEncrypted() && logname.length() > 3) {
            logname = CryptoHelper.decrypt(logname);
        }
        return logname;
    }

    private static String getStoredPwd(final SettingsAuthn settingsAuthn) {
        String pwd = settingsAuthn.getCred().getPwd();
        if (settingsAuthn.getCred().getEncrypted() && pwd.length() > 3) {
            pwd = CryptoHelper.decrypt(pwd);
        }
        return pwd;
    }

    private static String getTostoreLogname(final DialogLogin dialog) {
        String logname = dialog.getLoginname();
        if (dialog.getEncryptcred()) {
            logname = CryptoHelper.encrypt(logname);
        }
        return logname;
    }

    private static String getTostorePwd(final DialogLogin dialog) {
        String pwd = dialog.getPwd();
        if (dialog.getEncryptcred() && pwd.length() > 3) {
            pwd = CryptoHelper.encrypt(pwd);
        }
        return pwd;
    }

    /**
     * factory method to create the login dialog appropriate for the
     * GUI toolkit chosen.
     *
     * @return the login dialog created
     */
    private static DialogLogin createDialog() {
        DialogLogin dialog = null;
        final Application client = ApplicationManager.getApplication();
        final ApplicationGuiType guitype = client.getConfiguration().getGuitype();
        boolean savecred = false;
        boolean encryptcred = false;
        final SettingsAll settings = client.getSettings();
        SettingsAuthn settingsAuthn = null;
        if (settings != null) {
            settingsAuthn = settings.getAuthn();
        }
        if (settingsAuthn != null) {
            if (settingsAuthn.getCred() != null) {
                savecred = true;
            }
            final SettingsCred credsettings = settingsAuthn.getCred();
            if (credsettings != null) {
                if (credsettings.getPropValue("encrypted") == null) {
                    credsettings.setEncrypted(false);
                    encryptcred = true;
                } else if (credsettings.getEncrypted()) {
                    encryptcred = true;                    
                }
            }
        }
        switch (guitype) {
        case swing:
            dialog = new DialogLoginSwing(savecred, encryptcred);
            break;
        default:
            throw new RapidBeansRuntimeException("gui type \""
                    + guitype.name() + "\" not supported");
        }
        if (settingsAuthn != null
                && settingsAuthn.getCred() != null) {
            dialog.setLoginname(getStoredLogname(settingsAuthn));
            dialog.setPwd(getStoredPwd(settingsAuthn));
        }
        return dialog;
    }

    /**
     * business logic of a single login try.
     *
     * @param tryno the number of the concrete login try
     *        counted backwards
     *
     * @return the result of the login dialog
     */
    private LoginDialogResult tryLogin(
            final int tryno) {
        final Application client = ApplicationManager.getApplication();
        boolean ok = showLogin();
        LoginDialogResult result = null;
        if (!ok) {
            result = LoginDialogResult.cancelled;
        }
        final String loginnameGiven = getLoginname();
        if (result == null && (loginnameGiven == null
                || loginnameGiven.equals(""))) {
            result = LoginDialogResult.nouser;
        }
        if (result == null) {
            if (client.getAuthnDoc() == null) {
                throw new RapidBeansRuntimeException(
                        "No authentication document specified");
            }
            this.user =
                client.getAuthnDoc().findBean(
                    "org.rapidbeans.security.User",
                    loginnameGiven);
        }
        if (result == null && this.user == null) {
            result = LoginDialogResult.unkownuser;
        }
        final String pwdGiven = getPwd();
        String pwdHashed = null;
        if ((result == null)
            && (pwdGiven != null && (!(pwdGiven.equals(""))))) {
            if (client.getConfiguration() == null) {
                log.warning("client.getConfiguration()");
            } else {
                if (client.getConfiguration().getAuthorization() == null) {
                    log.warning("client.getConfiguration().getAuthorization() == null");
                } else {
                    final String pwdHashAlg = client.getConfiguration().getAuthorization().getPwdhashalgorithm();
                    if (pwdHashAlg != null) {
                        pwdHashed = User.hashPwd(pwdGiven, pwdHashAlg);
                    } else {
                        pwdHashed = pwdGiven;
                    }
                }
            }
        }
        if (result == null
                && (pwdHashed == null || pwdHashed.equals(""))
                && user.getPropValue("pwd") != null) {
            result = LoginDialogResult.nopwd;
        }
        if (result == null) {
            result = LoginDialogResult.wrongpwd;
            if (user.getPropValue("pwd") == null) {
                if (pwdHashed == null || pwdHashed.equals("")) {
                    result = LoginDialogResult.ok;
                }
            } else if (user.getPropValue("pwd").equals(pwdHashed)) {
                result = LoginDialogResult.ok;                
            }
        }
        if (result != LoginDialogResult.nopwd
                || user.getPropValue("pwd") != null) {
            this.evalResult(result, tryno);
        }
        return result;
    }

    /**
     * Evaluate the login dialog result and show an appropriate massage.
     *
     * @param result the result of the login dialog
     * @param tryno the number of the concrete login try
     *
     * @return the result
     */
    private void evalResult(final LoginDialogResult result,
            final int tryno) {
        final Application client = ApplicationManager.getApplication();
        final RapidBeansLocale locale = client.getCurrentLocale();
        switch (result) {
        case ok:
            break;
        case cancelled:
            client.messageInfo(locale.getStringMessage("login.cancelled"),
                    this.getTitle());
            break;
        default:
            if (tryno > 2) {
                client.messageError(locale.getStringMessage(
                        "login.failed", Integer.toString(tryno - 1)),
                        this.getTitle());
            } else  if (tryno == 2) {
                client.messageError(locale.getStringMessage(
                        "login.failed.uno"),
                        this.getTitle());
            } else {
                client.messageError(locale.getStringMessage(
                        "login.failed.shut"),
                        this.getTitle());
            }
            break;
        }
    }

    /**
     * this GUI toolkit specific method pops up a login dialog.
     *
     * @return if the dialog has been finished with OK (true)
     *         or Cancel (false), Closing the dialog is interpreted
     *         as Cancel.
     */
    protected abstract boolean showLogin();

    /**
     * @return the login name entered
     */
    protected abstract String getLoginname();

    /**
     * @param l the login name
     */
    protected abstract void setLoginname(final String l);

    /**
     * Accesses the pwd entered and erases the
     * pwd input field.
     *
     * @return the pwd entered
     */
    protected abstract String getPwd();

    /**
     * @param p the pwd
     */
    protected abstract void setPwd(final String p);

    /**
     * @return if saving the credentials is desired or not.
     */
    protected abstract boolean getSavecred();

    /**
     * @return if encrypting the credentials is desired or not.
     */
    protected abstract boolean getEncryptcred();

    /**
     * Dispose the dialog widget.
     */
    protected abstract void dispose();
}
