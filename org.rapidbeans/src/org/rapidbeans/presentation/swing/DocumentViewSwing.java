/*
 * Rapid Beans Framework: DocumentViewSwing.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 02/14/2006
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

package org.rapidbeans.presentation.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.datasource.Filter;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.DocumentView;
import org.rapidbeans.presentation.EditorBean;


/**
 * Swing implementation for view of a bean document.
 *
 * @author Martin Bluemel
 */
public class DocumentViewSwing extends DocumentView {

    /**
     * the widget.
     */
    private JInternalFrame frame = new JInternalFrame();

    /**
     * the split pane.
     */
    private JSplitPane splitPane = new JSplitPane();

    /**
     * the editor panel.
     */
    private JTabbedPane editorPane = new JTabbedPane();

    /**
     * for testing reasons.
     *
     * @return the tabbed pane
     */
    protected JTabbedPane getEditorPane() {
        return this.editorPane;
    }

    /**
     * maps the presented tab title to an editor key.
     */
    private HashMap<JPanel, String> editorKeyMap = new HashMap<JPanel, String>();

    /**
     * @return the JTreeView.
     */
    public Object getWidget() {
        return this.frame;
    }

    /**
     * constructor.
     *
     * @param client the client
     * @param doc the document to show
     * @param docconfname the view's document configuration name
     * @param viewconfname the view's configuration name
     * @param filter the filter
     */
    public DocumentViewSwing(final Application client, final Document doc,
            final String docconfname, final String viewconfname,
            final Filter filter) {
        super(client, doc, docconfname, viewconfname, filter);

        ImageIcon icon = null;
        if (ApplicationManager.getApplication() != null
                && ApplicationManager.getApplication().getMainwindow() != null
                && ((MainWindowSwing) ApplicationManager.getApplication().getMainwindow()).getIconManager() != null) {
            icon = ((MainWindowSwing) ApplicationManager.getApplication().getMainwindow()).getIconManager().getIcon(doc.getRoot().getType());
        }
        if (icon != null) {
            this.frame.setFrameIcon(icon);
        }

        this.editorPane.addChangeListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent e) {
                selectCurrentlySelectedEditorInTreeView();
            }
        });
        this.frame.setLayout(new BorderLayout());
        this.frame.setMaximizable(true);
        this.frame.setClosable(true);
        this.frame.setIconifiable(true);
        this.frame.setResizable(true);
        this.updateTitle();
        if (client == null || !client.getTestMode()) {
            this.frame.setVisible(true);
        } else {
            this.frame.setVisible(true);
        }
        this.markAsChanged(doc.getChanged());
        Dimension mainFrameSize = ((JFrame) this.getClient().getMainwindow().getWidget()).getSize();
        this.frame.setSize(new Dimension(mainFrameSize.width - 10, mainFrameSize.height - 50));
        this.frame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        this.frame.addInternalFrameListener(new InternalFrameListener() {

            public void internalFrameActivated(final InternalFrameEvent e) {
            }

            public void internalFrameClosed(final InternalFrameEvent e) {
            }

            public void internalFrameClosing(final InternalFrameEvent e) {
                close();
                getMainwindow().updateToolbars();
            }

            public void internalFrameDeactivated(final InternalFrameEvent e) {
            }

            public void internalFrameDeiconified(final InternalFrameEvent e) {
                getMainwindow().updateToolbars();
            }

            public void internalFrameIconified(final InternalFrameEvent e) {
                getMainwindow().updateToolbars();
            }

            public void internalFrameOpened(final InternalFrameEvent e) {
                getMainwindow().updateToolbars();
            }
        });

        this.splitPane.add((Component) this.getTreeView().getWidget(), JSplitPane.LEFT);
        this.splitPane.add(this.editorPane, JSplitPane.RIGHT);
        this.frame.add(this.splitPane, BorderLayout.CENTER);
    }

    /**
     * handler for selected beans.
     *
     * @param keys the tree paths to identify the edited object
     * @param beans the selected beans
     *
     * @return the bean editor of the last bean edited
     */
    public EditorBean editBeans(final Object[] keys, final RapidBean[] beans) {
        EditorBean editor = null;
        for (int i = 0; i < keys.length; i++) {
            editor = this.getEditor(beans[i], false);
            if (editor == null) {
               editor = super.addBeanEditor(beans[i], null, keys[i], false);
               this.editorKeyMap.put((JPanel) editor.getWidget(),
                    beans[i].getType().getName() + "::" + beans[i].getIdString());
               final String tabTitle = editor.getTitle();
               final ImageIcon icon = ((MainWindowSwing)
                       ApplicationManager.getApplication().getMainwindow()).getIconManager().getIcon(beans[i].getType());
               this.editorPane.addTab(tabTitle, icon, (JPanel) editor.getWidget());
            }
        }
        this.editorPane.setSelectedComponent((Component) editor.getWidget());
        return editor;
    }

    /**
     * Update the document view's title.
     */
    protected void updateTitle() {
        final String oldTitle = this.frame.getTitle();
        if (oldTitle != null && oldTitle.length() > 0
                && oldTitle.charAt(0) == '*') {
            this.frame.setTitle("*" + this.getTitle());
        } else {
            this.frame.setTitle(this.getTitle());
        }
    }

    /**
     * create a bean.
     * @param key the tree path
     * @param parentBeanColProp the parent bean of the new bean
     *
     * @return the bean editor just created
     */
    public EditorBean createBean(final Object key,
            final PropertyCollection parentBeanColProp) {
        final boolean docChangedBefore = getDocument().getChanged();
        RapidBean newBean = RapidBeanImplStrict.createInstance(
                ((TypePropertyCollection) parentBeanColProp.getType()).getTargetType().getName());
        EditorBean editor = this.getEditor(newBean, true);
        if (editor == null) {
           editor = super.addBeanEditor(newBean, parentBeanColProp, key, true);
           this.editorKeyMap.put((JPanel) editor.getWidget(),
                newBean.getType().getName() + "::" + newBean.getIdString());
           this.editorPane.add(editor.getTitle(), (JPanel) editor.getWidget());
        }
        this.editorPane.setSelectedComponent((Component) editor.getWidget());
        if (!docChangedBefore) {
            this.getDocument().resetChanged();
            this.markAsChanged(false);
        }
        return editor;
    }

    /**
     * handler for closed bean editors.
     *
     * @param editor the editor to close
     */
    public void editorClosed(final EditorBean editor) {
        this.editorPane.remove((JPanel) editor.getWidget());
        this.editorKeyMap.remove((JPanel) editor.getWidget());
        super.closeBeanEditor(editor);
    }

    /**
     * @return the title of the selected tab
     */
    protected String getSelectedEditorKey() {
        int selIndex = this.editorPane.getSelectedIndex();
        if (selIndex == -1) {
            return null;
        } else {
            return this.editorKeyMap.get((JPanel) this.editorPane.getComponentAt(selIndex));
        }
    }

    /**
     * mark / unmark the document as changed.
     *
     * @param changed if changed or unchanged
     */
    public void markAsChanged(final boolean changed) {
        if (changed) {
            if (!this.frame.getTitle().startsWith("*")) {
                this.frame.setTitle("*" + this.frame.getTitle());
            }
        } else {
            if (this.frame.getTitle().startsWith("*")) {
                final String s = this.frame.getTitle();
                this.frame.setTitle(s.substring(1, s.length()));
            }
        }
    }

    /**
     * close the document view.
     *
     * @return if canceling is desired
     */
    public boolean close() {
        boolean cancel = super.close();
        if (!cancel) {
            this.frame.dispose();
        }
        return cancel;
    }

    @Override
    public int getDividerLocation() {
        return this.splitPane.getDividerLocation();
    }

    @Override
    public void setDividerLocation(final int location) {
        this.splitPane.setDividerLocation(location);
    }
}
