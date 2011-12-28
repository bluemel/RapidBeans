/*
 * Rapid Beans Framework: DocumentController.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 03/11/2007
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

import java.io.File;
import java.net.MalformedURLException;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.AuthorizationException;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.settings.SettingsAll;


/**
 * Static helper class to drive document interactions
 * like "Open, Save, Save As".
 *
 * @author Martin Bluemel
 */
public final class DocumentController {

    /**
     * prevent constructor from being used.
     */
    private DocumentController() {
    }

    /**
     * Drives the document action "Open" for a (local) file.
     *
     * @param docconfname the document configuration's name to use
     * @param viewconfname the view configuration's name to use
     */
    public static void open(final String docconfname,
            final String viewconfname) {
        final Application app = ApplicationManager.getApplication();
        final RapidBeansLocale locale = app.getCurrentLocale();
        final File docFile = FileChooser.chooseFile(
                locale.getStringGui("commongui.text.open"),
                FileChooserType.open,
                app.getSettings().getBasic().getFolderdoc(),
                locale.getStringGui("commongui.text.document"), "xml");
        if (docFile != null) {
            final Document doc = new Document(docFile);
            try {
                app.openDocumentView(doc, docconfname, viewconfname);
                app.getSettings().getBasic().setFolderdoc(docFile.getParentFile());
                app.getSettingsDoc().save();
                app.addDocumentOpenedToHistory(doc);
            } catch (AuthorizationException e) {
                app.messageError(
                        app.getCurrentLocale().getStringMessage(
                        "authorization.denied.document",
                        app.getAuthenticatedUser().getProperty("accountname").toString()),
                        app.getCurrentLocale().getStringMessage("authorization.denied.title")
                        );
            }
        }
    }

    /**
     * Drives the document action "Save".
     * The active document is saved.
     */
    public static void save() {
        save(ApplicationManager.getApplication().getActiveDocument());
    }

    /**
     * Drives the document action "Save" with the given document.
     *
     * @param document the document to save
     */
    public static void save(final Document document) {
        final Application app = ApplicationManager.getApplication();
        if (document != null && document.getChanged()) {
            final String defaultEncoding = app.getSettings().getBasic()
                    .getDefaultencoding().name();
            final boolean forceEncoding = (app.getSettings().getBasic()
                    .getDefaultencodingusage() == DefaultEncodingUsage.write);
            document.save(defaultEncoding, forceEncoding, null);
        }
    }

    /**
     * Drives the document action "Save As".
     * The active document is saved under a chosen name.
     */
    public static void saveAs() {
        saveAs(ApplicationManager.getApplication().getActiveDocument());
    }

    /**
     * Drives the document action "Save As" with the given document.
     *
     * @param document the document to save
     */
    public static void saveAs(final Document document) {
        final Application app = ApplicationManager.getApplication();
        final SettingsAll settings = app.getSettings();
        final RapidBeansLocale locale = app.getCurrentLocale();
        final File docFile = FileChooser.chooseFile(
                locale.getStringGui("commongui.text.saveas"),
                FileChooserType.save,
                settings.getBasic().getFolderdoc(),
                locale.getStringGui("commongui.text.document"), "xml");
        if (docFile != null) {
            if (document != null) {
                boolean save = true;
                if (docFile.exists()) {
                    save = app.messageYesNo(
                            locale.getStringGui("messagedialog.title.saveas.overwrite"),
                            locale.getStringMessage("messagedialog.saveas.overwrite",
                                    docFile.getAbsolutePath()));
                }
                if (save) {
                        try {
                            document.setUrl(docFile.toURI().toURL());
                        } catch (MalformedURLException e) {
                            throw new RapidBeansRuntimeException(e);
                        }
                    final String defaultEncoding = app.getSettings().getBasic()
                            .getDefaultencoding().name();
                    final boolean forceEncoding = (app.getSettings().getBasic()
                            .getDefaultencodingusage() == DefaultEncodingUsage.write);
                    document.save(defaultEncoding, forceEncoding, null);
                    app.getSettings().getBasic().setFolderdoc(docFile.getParentFile());
                    app.getSettingsDoc().save();
                }
            }
        }
    }
}
