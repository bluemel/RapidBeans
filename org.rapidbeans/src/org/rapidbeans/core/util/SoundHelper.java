/*
 * Rapid Beans Framework: SoundHelper.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/15/2005
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

package org.rapidbeans.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.rapidbeans.core.exception.UtilException;

/**
 * A Utility Class for playing a sound file.
 * 
 * @author Martin Bluemel
 */
public final class SoundHelper {

	/**
	 * plays a sound file (asynchronously).
	 * 
	 * @param soundfile
	 *            the file with the sound to play.
	 */
	public static void play(final File soundfile) {
		try {
			play(new FileInputStream(soundfile));
		} catch (FileNotFoundException e) {
			throw new UtilException("sound file not found: \""
					+ soundfile.getAbsolutePath() + "\"", e);
		} catch (UtilException e) {
			Throwable eNested = e.getCause();
			if (eNested instanceof IOException) {
				throw new UtilException(
						"IO exception while trying to play sound file  \""
								+ soundfile.getAbsolutePath() + "\"", eNested);
			} else if (eNested instanceof UnsupportedAudioFileException) {
				throw new UtilException("unsupported audio file: \""
						+ soundfile.getAbsolutePath() + "\"", eNested);
			} else {
				throw e;
			}
		}
	}

	/**
	 * plays a sound file (asynchronously).
	 * 
	 * @param instream
	 *            the file with the sound to play.
	 */
	public static void play(final InputStream instream) {
		try {
			final AudioInputStream stream = AudioSystem
					.getAudioInputStream(instream);
			final AudioFormat format = stream.getFormat();
			final DataLine.Info info = new DataLine.Info(Clip.class, format);
			final Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			throw new UtilException(e);
		} catch (IOException e) {
			throw new UtilException(e);
		} catch (LineUnavailableException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * prevent the default constructor from being used.
	 */
	private SoundHelper() {
	}
}
