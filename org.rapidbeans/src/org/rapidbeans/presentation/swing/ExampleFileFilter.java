/*
 * Rapid Beans Framework: ExampleFileFilter.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 08/01/2007
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

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

/**
 * A convenience implementation of FileFilter that filters out all files except
 * for those type extensions that it knows about.
 *
 * Extensions are of the type ".foo", which is typically found on Windows and
 * Unix boxes, but not on Macinthosh. Case is ignored.
 *
 * Example - create a new filter that filerts out all files but gif and jpg
 * image files:
 *
 * JFileChooser chooser = new JFileChooser(); ExampleFileFilter filter = new
 * ExampleFileFilter( new String{"gif", "jpg"}, "JPEG & GIF Images")
 * chooser.addChoosableFileFilter(filter); chooser.showOpenDialog(this);
 *
 * @version 1.16 07/26/04
 * @author Jeff Dinkins
 */
public final class ExampleFileFilter extends FileFilter {

    /**
     * filters.
     */
    private Hashtable<String,ExampleFileFilter> filters = null;

    /**
     * description of the files to filter.
     */
    private String description = null;

    /**
     * full description of the files to filter.
     */
    private String fullDescription = null;

    /**
     * if extension shoudl be used.
     */
    private boolean useExtensionsInDescription = true;

    /**
     * Creates a file filter. If no filters are added, then all files are
     * accepted.
     *
     * @see #addExtension
     */
    public ExampleFileFilter() {
        this.filters = new Hashtable<String,ExampleFileFilter>();
    }

    /**
     * Creates a file filter that accepts files with the given extension.
     * Example: new ExampleFileFilter("jpg");
     *
     * @param extension the extension
     *
     * @see #addExtension
     */
    public ExampleFileFilter(final String extension) {
        this(extension, null);
    }

    /**
     * Creates a file filter that accepts the given file type. Example: new
     * ExampleFileFilter("jpg", "JPEG Image Images");
     *
     * Note that the "." before the extension is not needed. If provided, it
     * will be ignored.
     *
     * @param extension the file extension to be filtered
     * @param descr the description of the file extension
     *
     * @see #addExtension
     */
    public ExampleFileFilter(final String extension, final String descr) {
        this();
        if (extension != null) {
            addExtension(extension);
        }
        if (descr != null) {
            setDescription(descr);
        }
    }

    /**
     * Creates a file filter from the given string array. Example: new
     * ExampleFileFilter(String {"gif", "jpg"});
     *
     * Note that the "." before the extension is not needed adn will be ignored.
     *
     * @param argFilters the filters
     *
     * @see #addExtension
     */
    public ExampleFileFilter(final String[] argFilters) {
        this(argFilters, null);
    }

    /**
     * Creates a file filter from the given string array and description.
     * Example: new ExampleFileFilter(String {"gif", "jpg"}, "Gif and JPG
     * Images");
     *
     * Note that the "." before the extension is not needed and will be ignored.
     *
     * @param argFilters the filters
     * @param argDescription the description
     *
     * @see #addExtension
     */
    public ExampleFileFilter(final String[] argFilters, final String argDescription) {
        this();
        for (int i = 0; i < argFilters.length; i++) {
            // add filters one by one
            addExtension(argFilters[i]);
        }
        if (argDescription != null) {
            setDescription(argDescription);
        }
    }

    /**
     * Return true if this file should be shown in the directory pane, false if
     * it shouldn't.
     *
     * @param f the file
     * Files that begin with "." are ignored.
     * @return if the file can be accepted or not
     *
     * @see #getExtension
     * @see FileFilter#accepts
     */
    public boolean accept(final File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null && this.filters.get(getExtension(f)) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param f the file
     * @return the extension portion of the file's name .
     *
     * @see #getExtension
     * @see FileFilter#accept
     */
    public String getExtension(final File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    /**
     * Adds a filetype "dot" extension to filter against.
     *
     * @param extension the extension to filter for
     * For example: the following code will create a filter that filters out all
     * files except those that end in ".jpg" and ".tif":
     *
     * ExampleFileFilter filter = new ExampleFileFilter();
     * filter.addExtension("jpg"); filter.addExtension("tif");
     *
     * Note that the "." before the extension is not needed and will be ignored.
     */
    public void addExtension(final String extension) {
        if (this.filters == null) {
            this.filters = new Hashtable<String,ExampleFileFilter>(5);
        }
        this.filters.put(extension.toLowerCase(), this);
        this.fullDescription = null;
    }

    /**
     * @return the human readable description of this filter. For example: "JPEG
     * and GIF Image Files (*.jpg, *.gif)"
     *
     * @see setDescription
     * @see setExtensionListInDescription
     * @see isExtensionListInDescription
     * @see FileFilter#getDescription
     */
    public String getDescription() {
        if (this.fullDescription == null) {
            if (this.description == null || isExtensionListInDescription()) {
                //this.fullDescription = this.description == null ? "(" : description + " (";
                if (this.description == null) {
                    this.fullDescription = "(";
                } else {
                    this.fullDescription = this.description + " (";
                }
                // build the description from the extension list
                Enumeration<String> extensions = this.filters.keys();
                if (extensions != null) {
                    this.fullDescription += "." + (String) extensions.nextElement();
                    while (extensions.hasMoreElements()) {
                        this.fullDescription += ", ." + extensions.nextElement();
                    }
                }
                this.fullDescription += ")";
            } else {
                this.fullDescription = this.description;
            }
        }
        return this.fullDescription;
    }

    /**
     * Sets the human readable description of this filter. For example:
     * filter.setDescription("Gif and JPG Images");
     *
     * @param argDescription the description of this filter
     * @see setDescription
     * @see setExtensionListInDescription
     * @see isExtensionListInDescription
     */
    public void setDescription(final String argDescription) {
        this.description = argDescription;
        this.fullDescription = null;
    }

    /**
     * Determines whether the extension list (.jpg, .gif, etc) should show up in
     * the human readable description.
     *
     * Only relevent if a description was provided in the constructor or using
     * setDescription();
     *
     * @param b the new value for property (use) ExtensionsInDescription)
     *
     * @see getDescription
     * @see setDescription
     * @see isExtensionListInDescription
     */
    public void setExtensionListInDescription(final boolean b) {
        this.useExtensionsInDescription = b;
        this.fullDescription = null;
    }

    /**
     * Returns whether the extension list (.jpg, .gif, etc) should show up in
     * the human readable description.
     *
     * Only relevent if a description was provided in the constructor or using
     * setDescription();
     * @return property (use) ExtensionsInDescription
     * @see getDescription
     * @see setDescription
     * @see setExtensionListInDescription
     */
    public boolean isExtensionListInDescription() {
        return this.useExtensionsInDescription;
    }
}

