/*
 * Rapid Beans Framework: MainWindowSwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/31/2005
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.DocumentView;
import org.rapidbeans.presentation.MainWindow;
import org.rapidbeans.presentation.Toolbar;
import org.rapidbeans.presentation.View;
import org.rapidbeans.presentation.config.ConfigMainWindow;
import org.rapidbeans.presentation.guistate.MainWindowState;
import org.rapidbeans.presentation.guistate.UiState;
import org.rapidbeans.service.ActionQuit;
import org.rapidbeans.service.CursorStyle;

/**
 * @author Martin Bluemel
 */
public final class MainWindowSwing extends MainWindow {

	private static final int DEFAULT_HEIGHT = 600;

	private static final int DEFAULT_WIDTH = 800;

	/**
	 * background color for selected items.
	 */
	public static final Color COLOR_SELECTED_BACKGROUND = new JList<Object>().getSelectionBackground();

	/**
	 * the frame instance.
	 */
	private JFrame frame = null;

	/**
	 * the desktop pane.
	 */
	private JDesktopPane desktopPane = null;

	/**
	 * The north toolbar panel.
	 */
	private JPanel toolbarPanelNorth = new JPanel();

	/**
	 * @return the JFrame widget
	 */
	public Object getWidget() {
		return this.frame;
	}

	/**
	 * show the main window.
	 */
	public void show() {
		final Application client = ApplicationManager.getApplication();
		if (client == null || !client.getTestMode()) {
			this.frame.setVisible(true);
		} else {
			this.frame.setVisible(false);
		}
	}

	/**
	 * close the main window.
	 */
	public void close() {
		this.frame.dispose();
	}

	/**
	 * add a view (internal frame) to the main window.
	 * 
	 * @param view the view to add
	 */
	public void addView(final View view) {
		addView(ApplicationManager.getApplication(), view);
	}

	/**
	 * add a view (internal frame) to the main window.
	 * 
	 * @param client the client
	 * @param view   the view to add
	 */
	public void addView(final Application client, final View view) {
		JComponent widget = (JComponent) view.getWidget();
		this.desktopPane.add(widget);
		if (widget instanceof JInternalFrame) {
			JInternalFrame iframe = (JInternalFrame) widget;
			try {
				switch (client.getSettings().getBasic().getGui().getDocViewOpenWindowBehaviour()) {
				case maximized:
					iframe.setMaximum(true);
					break;
				default:
					break;
				}
				putToFront(view);
			} catch (PropertyVetoException e) {
				throw new RapidBeansRuntimeException(e);
			}
		}
		updateToolbars();
	}

	/**
	 * Puts the given view to the front so that it can be seen and edited.
	 * 
	 * @param view the view to put to front
	 */
	public void putToFront(final View view) {
		JComponent widget = (JComponent) view.getWidget();
		if (widget instanceof JInternalFrame) {
			JInternalFrame iframe = (JInternalFrame) widget;
			try {
				if (iframe.isIcon()) {
					iframe.setIcon(false);
				} else {
					if (iframe.isMaximum()) {
						iframe.setMaximum(false);
						iframe.setMaximum(true);
					} else {
						iframe.setIcon(true);
						iframe.setIcon(false);
					}
				}
			} catch (PropertyVetoException e) {
				throw new RapidBeansRuntimeException(e);
			}
		}
		updateToolbars();
	}

	/**
	 * add a view (internal frame) to the main window.
	 * 
	 * @param view the view to add
	 */
	public void removeView(final View view) {
		((JInternalFrame) view.getWidget()).dispose();
		this.desktopPane.remove((JComponent) view.getWidget());
		updateToolbars();
	}

	/**
	 * retrieve the document currently on top and active.
	 * 
	 * @return the active document
	 */
	public DocumentView getActiveDocumentView() {
		DocumentView activeDocumentView = null;
		JInternalFrame topFrame = null;
		if (this.desktopPane == null) {
			return null;
		}
		for (Component comp : this.desktopPane.getComponents()) {
			if (comp instanceof JInternalFrame && this.desktopPane.getComponentZOrder(comp) == 0) {
				topFrame = (JInternalFrame) comp;
				break;
			}
		}
		if (topFrame != null) {
			activeDocumentView = (DocumentView) ApplicationManager.getApplication().getViewByWidget(topFrame);
		}
		return activeDocumentView;
	}

	/**
	 * retrieve the document currently on top and active.
	 * 
	 * @return the active document
	 */
	public Document getActiveDocument() {
		DocumentView activeDocumentView = this.getActiveDocumentView();
		if (activeDocumentView == null) {
			return null;
		} else {
			return this.getActiveDocumentView().getDocument();
		}
	}

	/**
	 * @return the main window's icon manager instance
	 */
	public synchronized IconManagerSwing getIconManager() {
		if (this.iconManager == null) {
			this.iconManager = new IconManagerSwing();
		}
		return this.iconManager;
	}

	/**
	 * construct a MainWindowSwing.
	 * 
	 * @param client           the parent client
	 * @param mainWindowConfig the configuration
	 */
	public MainWindowSwing(final Application client, final ConfigMainWindow mainWindowConfig) {
		super(client, mainWindowConfig);

		this.frame = new JFrame();
		if (mainWindowConfig == null) {
			this.frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		} else {
			this.frame.setSize(mainWindowConfig.getWidth(), mainWindowConfig.getHeight());
		}
		this.frame.setTitle(getWindowTitle(client));
		final Image image = getIconManager().getImage("mainwindow.icon");
		if (image != null) {
			this.frame.setIconImage(image);
		}
		this.frame.getContentPane().setLayout(new BorderLayout());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.addWindowListener(new WindowAdapter() {
			private ActionQuit actionHandlerQuit = new ActionQuit();

			public void windowClosing(final WindowEvent e) {
				this.actionHandlerQuit.execute();
				if (!this.actionHandlerQuit.isCancelled()) {
					frame.dispose();
				}
			}
		});

		if (this.getMenubar() != null) {
			final JMenuBar menuBar = (JMenuBar) ((MenubarSwing) this.getMenubar()).getWidget();
			this.frame.setJMenuBar(menuBar);
		}

		if (this.getToolbars().size() > 1) {
			this.toolbarPanelNorth.setLayout(new FlowLayout(FlowLayout.LEFT));
			this.frame.getContentPane().add(this.toolbarPanelNorth, BorderLayout.NORTH);
		}
		for (final Toolbar toolbar : this.getToolbars()) {
			final JToolBar toolBar = (JToolBar) ((ToolbarSwing) toolbar).getWidget();
			if (this.getToolbars().size() > 1) {
				this.toolbarPanelNorth.add(toolBar);
			} else {
				this.frame.getContentPane().add(toolBar, BorderLayout.NORTH);
			}
		}

		this.desktopPane = new JDesktopPane();
		this.desktopPane.setLayout(null);
		this.frame.getContentPane().add(this.desktopPane, BorderLayout.CENTER);

		this.frame.getContentPane().add((JPanel) this.getFooter().getWidget(), BorderLayout.SOUTH);
	}

	private IconManagerSwing iconManager = null;

	/**
	 * @return the Z order if the view is a top level view. -1 otherwise.
	 */
	@Override
	public int getViewZOrder(final View view) {
		return this.desktopPane.getComponentZOrder((JComponent) view.getWidget());
	}

	@Override
	public int getLocationX() {
		return this.frame.getX();
	}

	@Override
	public int getLocationY() {
		return this.frame.getY();
	}

	@Override
	public int getHeight() {
		return this.frame.getHeight();
	}

	@Override
	public int getWidth() {
		return this.frame.getWidth();
	}

	@Override
	public MainWindowState saveUiState(final UiState uiState) {
		final MainWindowState mainWinState = new MainWindowState();
		mainWinState.setLocationX(this.frame.getX());
		mainWinState.setLocationY(this.frame.getY());
		mainWinState.setHeight(this.frame.getHeight());
		mainWinState.setWidth(this.frame.getWidth());
		return mainWinState;
	}

	@Override
	public void restoreUiState(final UiState uiState) {
		final MainWindowState mainWinState = uiState.getMainWindow();
		if (mainWinState != null) {
			this.frame.setBounds(mainWinState.getLocationX(), mainWinState.getLocationY(), mainWinState.getWidth(),
					mainWinState.getHeight());
		}
	}

	private final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

	private final static Cursor defaultCursor = Cursor.getDefaultCursor();

	/**
	 * Set the cursor to a defined style.
	 * 
	 * @param style the cursor style to set.
	 */
	@Override
	public void setCursor(final CursorStyle style) {
		Cursor cursor = null;
		switch (style) {
		case defaultcursor:
			cursor = defaultCursor;
			break;
		case wait:
			cursor = waitCursor;
			break;
		default:
			throw new RapidBeansRuntimeException("invalid cursor style " + style.toString());
		}
		setComponentCursor(this.frame, cursor);
	}

	private void setComponentCursor(final Component component, final Cursor cursor) {
		component.setCursor(cursor);
		if (component instanceof Container) {
			final Container cont = (Container) component;
			for (final Component comp : cont.getComponents()) {
				setComponentCursor(comp, cursor);
			}
		}
	}
}
