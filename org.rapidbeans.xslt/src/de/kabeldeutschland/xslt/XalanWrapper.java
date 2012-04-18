package de.kabeldeutschland.xslt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * The Java wrapper class for executing and XSL transformation with XalanJ.
 * 
 * @author Martin Bluemel
 */
public final class XalanWrapper {

	private static final String DQUOTE = "\"";

	private static final int ARG_COUNT_EXPECTED = 3;

	private static final int ERROR_CODE_INVALID_OPTION = 1;

	private static final int ERROR_CODE_ARGUMENT_COUNT = 2;

	private static final int ERROR_CODE_FILE = 3;

	private static final int ERROR_CODE_EXCEPTION = 4;

	/**
	 * Prevent using the default constructor.
	 */
	private XalanWrapper() {
	}

	/**
	 * The Java wrapper method for executing and XSL transformation with XalanJ.
	 * 
	 * @param args
	 *            the command line arguments.
	 */
	public static void main(String[] args) {

		int argCount = 0;
		String sIn = null;
		String sStyle = null;
		String sOut = null;
		final Map<String, String> parameterMap = new HashMap<String, String>();

		for (final String arg : args) {
			if (arg.startsWith("-")) {
				if (arg.startsWith("-p")) {
					final String nameAndVal = arg.substring(2);
					final List<String> tokens = split(nameAndVal, "=");
					if (!(tokens.size() == 2)) {
						System.err.println("ERROR: invalid parameter option \""
								+ arg + DQUOTE);
						printUsage();
						System.exit(ERROR_CODE_INVALID_OPTION);
					}
					parameterMap.put(tokens.get(0), tokens.get(1));
				} else if (arg == "-h" || arg == "--h" || arg == "-help"
						|| arg == "--help") {
					printUsage();
				} else {
					System.err.println("ERROR: invalid option \"" + arg
							+ DQUOTE);
					printUsage();
					System.exit(ERROR_CODE_INVALID_OPTION);
				}
			} else {
				switch (argCount) {
				case 0:
					sIn = arg;
					argCount++;
					break;
				case 1:
					sStyle = arg;
					argCount++;
					break;
				case 2:
					sOut = arg;
					argCount++;
					break;
				default:
					System.err.println("ERROR: too much arguments");
					printUsage();
					System.exit(ERROR_CODE_ARGUMENT_COUNT);
				}
			}
		}

		if (argCount < ARG_COUNT_EXPECTED) {
			System.err.println("ERROR: too less arguments");
			printUsage();
			System.exit(ERROR_CODE_ARGUMENT_COUNT);
		}

		final File in = new File(sIn);
		final File style = new File(sStyle);
		final File out = new File(sOut);

		final int ret = xslt(in, style, out, parameterMap);
		System.exit(ret);
	}

	/**
	 * @param in
	 *            the XML file
	 * @param style
	 *            the XSLT style sheet
	 * @param out
	 *            the output file
	 * @param parameters
	 *            the parameter map
	 * 
	 * @return the error code or 0 is succeeded
	 * 
	 * @throws TransformerFactoryConfigurationError
	 *             in case of XML / XSLT transformer problems
	 */
	public static int xslt(final File in, final File style, final File out,
			final Map<String, String> parameters)
			throws TransformerFactoryConfigurationError {
		int error = 0;
		try {
			Map<String, String> parameterMap = parameters;
			if (parameterMap == null) {
				parameterMap = new HashMap<String, String>();
			}
			if (!in.exists()) {
				System.err.println("ERROR: can't find input XML file \""
						+ in.getAbsolutePath() + DQUOTE);
				printUsage();
				error = ERROR_CODE_FILE;
			}
			if (error == 0 && !in.canRead()) {
				System.err.println("ERROR: can't read input XML file \""
						+ in.getAbsolutePath() + DQUOTE);
				printUsage();
				error = ERROR_CODE_FILE;
			}
			if (error == 0 && !style.exists()) {
				System.err.println("ERROR: can't find XSL style sheet file \""
						+ style.getAbsolutePath() + DQUOTE);
				printUsage();
				error = ERROR_CODE_ARGUMENT_COUNT;
			}
			if (error == 0 && !style.canRead()) {
				System.err.println("ERROR: can't read XSL style sheet file \""
						+ style.getAbsolutePath() + DQUOTE);
				printUsage();
				error = ERROR_CODE_FILE;
			}
			if (error == 0 && !out.getParentFile().exists()) {
				if (!out.getParentFile().mkdirs()) {
					System.err
							.println("ERROR: can't create parent folder for output file \""
									+ style.getAbsolutePath() + DQUOTE);
					error = ERROR_CODE_ARGUMENT_COUNT;
				}
			}
			if (error == 0 && out.exists() && (!out.canWrite())) {
				System.err.println("ERROR: can't write \""
						+ style.getAbsolutePath() + DQUOTE);
				error = ERROR_CODE_ARGUMENT_COUNT;
			}

			if (error == 0) {
				TransformerFactory tFactory =
						javax.xml.transform.TransformerFactory.newInstance();
				Transformer transformer =
						tFactory.newTransformer(new StreamSource(style));
				for (final Entry<String, String> param : parameterMap
						.entrySet()) {
					transformer.setParameter(param.getKey(), param.getValue());
				}
				transformer.transform(new StreamSource(in), new StreamResult(
						new FileOutputStream(out)));
			}
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			error = ERROR_CODE_EXCEPTION;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			error = ERROR_CODE_EXCEPTION;
		} catch (TransformerException e) {
			e.printStackTrace();
			error = ERROR_CODE_EXCEPTION;
		} catch (RuntimeException e) {
			e.printStackTrace();
			error = ERROR_CODE_EXCEPTION;
		}
		return error;
	}

    /**
     * Splits a string into tokens using one or multiple delimiter characters.
     *
     * @param string the string to spit.
     * @param delimChars a string containing all delimiter characters
     *
     * @return a list containing all tokens
     */
    private static List<String> split(final String string, final String delimChars) {
        final ArrayList<String> list = new ArrayList<String>();
        final StringTokenizer tokenizer = new StringTokenizer(string, delimChars);
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }

	/**
	 * Print the command line help.
	 */
	private static void printUsage() {
		System.out
				.println("  Usage: xslt [ <parameter option> ... ] <in> <style> <out>");
		System.out.println("    in: the input XML file");
		System.out.println("    style: the XSL style sheet file");
		System.out.println("    out: the output file");
		System.out.println("    parameter option: -p<name>=<value>");
	}
}
