/*
 * Rapid Beans Framework, SDK: XslTransformer.java
 * 
 * Copyright (C) 2018 Martin Bluemel
 * 
 * Creation Date: 08/10/2018
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

package org.rapidbeans.sdk.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.rapidbeans.sdk.utils.BuildException;

/**
 * Convenience wrapper class for executing and XSL transformation with XalanJ.
 * Performance boost by reusing of Transformers with on and the same style
 * sheet.
 * 
 * @author Martin Bluemel
 */
public final class XslTransformer {

	private final TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();

	// Maps style sheet absolute paths to Transformers already instantiated
	private Map<String, Transformer> transMap = new HashMap<String, Transformer>();

	public XslTransformer() {
	}

	/**
	 * @param in         the XML file
	 * @param style      the XSLT style sheet
	 * @param out        the output file
	 * @param parameters the parameter map
	 * 
	 * @return the error code or 0 is succeeded
	 * 
	 * @throws TransformerFactoryConfigurationError in case of XML / XSLT
	 *                                              transformer problems
	 */
	public void transform(final File in, final File style, final File out, final Map<String, String> parameters)
			throws TransformerFactoryConfigurationError {
		validateIn(in);
		validateStyle(style);
		validateAndMkdirsOut(out);
		final Map<String, String> parameterMap = initializeEmptyIfNull(parameters);
		try (final FileOutputStream fosOut = new FileOutputStream(out)) {
			final Transformer transformer = getOrCreateTransformer(style);
			addParameters(parameterMap, transformer);
			transformer.transform(new StreamSource(in), new StreamResult(fosOut));
		} catch (TransformerException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	private void addParameters(final Map<String, String> parameterMap, final Transformer transformer) {
		for (final Entry<String, String> param : parameterMap.entrySet()) {
			transformer.setParameter(param.getKey(), param.getValue());
		}
	}

	private Transformer getOrCreateTransformer(final File style) throws TransformerConfigurationException, IOException {
		final String styleKey = style.getCanonicalPath();
		Transformer transformer = this.transMap.get(styleKey);
		if (transformer == null) {
			transformer = tFactory.newTransformer(new StreamSource(style));
			this.transMap.put(styleKey, transformer);
		} else {
			transformer.clearParameters();
		}
		return transformer;
	}

	private static Map<String, String> initializeEmptyIfNull(final Map<String, String> parameters) {
		Map<String, String> parameterMap = parameters;
		if (parameterMap == null) {
			parameterMap = new HashMap<String, String>();
		}
		return parameterMap;
	}

	private static void validateAndMkdirsOut(final File out) {
		if (!out.getParentFile().exists()) {
			if (!out.getParentFile().mkdirs()) {
				throw new IllegalArgumentException(String
						.format("ERROR: can't create parent folder for output file \"%s\"", out.getAbsolutePath()));
			}
		}
		if (out.exists() && (!out.canWrite())) {
			throw new IllegalArgumentException(
					String.format("ERROR: can't overwrite existing output file \"%s\"", out.getAbsolutePath()));
		}
	}

	private static void validateStyle(final File style) {
		if (!style.exists()) {
			throw new IllegalArgumentException(
					String.format("ERROR: can't find XSL style sheet file \"%s\"", style.getAbsolutePath()));
		}
		if (!style.canRead()) {
			throw new IllegalArgumentException(
					String.format("ERROR: can't read XSL style sheet file \"%s\"", style.getAbsolutePath()));
		}
	}

	private static void validateIn(final File in) {
		if (!in.exists()) {
			throw new IllegalArgumentException(
					String.format("ERROR: can't find input XML file \"%s\"", in.getAbsolutePath()));
		}
		if (!in.canRead()) {
			throw new IllegalArgumentException(
					String.format("ERROR: can't read input XML file \"%s\"", in.getAbsolutePath()));
		}
	}
}
