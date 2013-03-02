/*
 * Rapid Beans Framework: IconManagerSwing
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 04/01/2010
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

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.UiProperties;

public class IconManagerSwing {

	private UiProperties uiProps = null;

	private Map<Object, ImageIcon> iconMap = null;

	private Map<Object, Image> imageMap = null;

	/**
	 * constructor.
	 */
	public IconManagerSwing() {
		this.iconMap = new HashMap<Object, ImageIcon>();
		this.imageMap = new HashMap<Object, Image>();
		this.uiProps = ApplicationManager.getApplication().getUiProps();
	}

	/**
	 * Provide an image icon loaded from program resource.
	 * 
	 * @param bean
	 *            the bean for which to provide the icon
	 * 
	 * @return the image icon
	 */
	public ImageIcon getIcon(final TypeRapidBean beantype) {
		final String key = "bean." + beantype.getName().toLowerCase() + ".icon";
		return getIcon(key);
	}

	public Image getImage(final String key) {
		Image image = this.imageMap.get(key);
		if (image != null) {
			return image;
		}
		if (this.uiProps == null) {
			return null;
		}
		final String value = this.uiProps.getPropertyValue(key);
		if (value == null) {
			return null;
		}
		URL url = ApplicationManager.getApplication().getClass()
				.getResource(value);
		if (url == null) {
			url = ClassLoader.getSystemResource(value);
		}
		if (url == null) {
			throw new MissingResourceException(
					"Image resource file not found:\n" + "  key = \"" + key
							+ "\", value = \"" + value + "\"\n"
							+ "  see resource file presentation/gui.properties",
					key, value);
		}
		try {
			image = ImageIO.read(url);
		} catch (IOException e) {
			throw new RapidBeansRuntimeException(e);
		}
		this.imageMap.put(key, image);
		return image;
	}

	public ImageIcon getIcon(final String key) {
		ImageIcon icon = this.iconMap.get(key);
		if (icon != null) {
			return icon;
		}
		if (this.uiProps == null) {
			return null;
		}
		final String value = this.uiProps.getPropertyValue(key);
		if (value == null) {
			return null;
		}
		final Application app = ApplicationManager.getApplication();
		URL url = ClassLoader.getSystemResource(value);
		if (url == null) {
			url = ClassLoader.getSystemResource(app.getRootpackage().replace(
					'.', '/')
					+ "/presentation/" + value);
		}
		if (url == null) {
			url = ApplicationManager.getApplication().getClass()
					.getResource(value);
		}
		if (url == null) {
			throw new MissingResourceException(
					"Icon resource file not found:\n" + "  key = \"" + key
							+ "\", value = \"" + value + "\"\n"
							+ "  see resource file presentation/gui.properties",
					key, value);
		}
		icon = new ImageIcon(url);
		this.iconMap.put(key, icon);
		return icon;
	}
}
