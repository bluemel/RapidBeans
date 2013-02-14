/*
 * Rapid Beans Framework: EditorPropertyDateSwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/13/2006
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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JTextField;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyDate;
import org.rapidbeans.core.common.PrecisionDate;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypePropertyDate;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;

/**
 * a special property editor for Date properties.
 * Has a text field and later on will be extended with
 * a calendar widget.
 * 
 * @author Martin Bluemel
 */
public class EditorPropertyDateSwing extends EditorPropertySwing {

	/**
	 * the text field.
	 */
	private JTextField text = new JTextField();

	/**
	 * @return the editor's widget
	 */
	public Object getWidget() {
		return this.text;
	}

	/**
	 * constructor.
	 * 
	 * @param prop
	 *            the bean property to edit
	 * @param propBak
	 *            the bean property backup
	 * @param bizBeanEditor
	 *            the parent bean editor
	 * @param client
	 *            the client
	 */
	public EditorPropertyDateSwing(final Application client,
			final EditorBean bizBeanEditor,
			final Property prop, final Property propBak) {
		super(client, bizBeanEditor, prop, propBak);
		super.initColors();
		if (!(prop instanceof PropertyDate)) {
			throw new RapidBeansRuntimeException("invalid propperty for a date editor");
		}
		if (prop.getType().isKeyCandidate()
				&& (!this.getBeanEditor().isInNewMode())) {
			this.text.setEditable(false);
		}
		this.text.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(final KeyEvent e) {
			}

			@Override
			public void keyPressed(final KeyEvent e) {
			}

			@Override
			public void keyReleased(final KeyEvent e) {
				handleKeyReleased(e);
			}
		});
		this.text.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				handleFocusLost();
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});
		this.updateUI();
		final ConfigPropEditorBean cfg = getConfig();
		if (prop.getReadonly()
				|| (cfg != null && !cfg.getEnabled())) {
			this.text.setEnabled(false);
		}
	}

	public void handleKeyReleased(final KeyEvent e) {
		try {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_UP:
			case KeyEvent.VK_TAB:
				break;
			default:
				setUIUpdateLock();
				break;
			}
			fireInputFieldChanged();
		} finally {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_UP:
			case KeyEvent.VK_TAB:
				updateUI();
				break;
			default:
				releaseUIUpdateLock();
				break;
			}
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_DOWN:
			getBeanEditor().rotateFocus(getProperty(), EditorBean.DIRECTION_DOWN);
			break;
		case KeyEvent.VK_UP:
			getBeanEditor().rotateFocus(getProperty(), EditorBean.DIRECTION_UP);
			break;
		default:
			break;
		}
	}

	public void handleFocusLost() {
		final Date dateOld = ((PropertyDate) getProperty()).getValue();
		final Date dateNew = (Date) getInputFieldValue();
		if ((dateOld == null && dateNew != null)
				|| (dateOld != null && dateNew == null)
				&& (dateOld != null && dateNew != null && !dateOld.equals(dateNew))) {
			fireInputFieldChanged();
		} else if (dateOld != null && dateNew != null
				&& dateOld.equals(dateNew)
				&& !this.text.equals(getInputFieldValueString())) {
			updateUI();
		}
	}

	/**
	 * update the string presented in the editor.
	 */
	public void updateUI() {
		if (this.getUIUpdateLock()) {
			return;
		}
		try {
			this.inputFieldValueCompleted = false;
			this.setUIEventLock();
			final Object value = this.getProperty().getValue();
			String newText = "";
			if (value != null) {
				newText = DateFormat.getDateInstance(DateFormat.MEDIUM,
						this.getLocale().getLocale()).format(value);
			}
			if (!this.text.getText().equals(newText)) {
				this.text.setText(newText);
			}
		} finally {
			this.releaseUIEventLock();
		}
	}

	/**
	 * @return the Text field's content
	 */
	public Object getInputFieldValue() {
		Date ifValue = null;
		String s = this.text.getText();
		if (s.trim().length() > 0) {
			if (this.getLocale().getName().equals("de")) {
				ifValue = readDate();
			} else {
				try {
					ifValue = DateFormat.getDateInstance(DateFormat.MEDIUM,
							this.getLocale().getLocale()).parse(s);
				} catch (ParseException e) {
					throw new ValidationException(SIG_DATE_INVALID_FORMAT,
							this,
							"invalid localized medium date string \"" + s + "\""
									+ " for locale " + this.getLocale().getLocale().getDisplayName());
				}
			}
		}
		return ifValue;
	}

	/**
	 * parse a gemen medium date.
	 * 
	 * [d]d.[m]m.[y][y][y]y
	 * 
	 * @return a date
	 */
	private Date readDate() {
		this.inputFieldValueCompleted = false;
		ParsedDate pd = this.parseLocalDate(true);
		this.inputFieldValueCompleted = pd.isCompleted();
		return pd.toDate(((TypePropertyDate)
				((PropertyDate) this.getProperty()).getType()).getPrecision());
	}

	/**
	 * signature for validation exception because of wrong date format.
	 */
	public static final String SIG_DATE_INVALID_FORMAT =
			"invalid.prop.date.string.local.format";

	/**
	 * signature for validation exception because of wrong date format.
	 */
	public static final String SIG_DATE_INVALID_ICOMPLETE =
			"invalid.prop.date.string.local.incomplete";

	/**
	 * signature for validation exception because of wrong date format.
	 */
	public static final String SIG_DATE_INVALID_DAY =
			"invalid.prop.date.string.local.day";

	/**
	 * signature for validation exception because of wrong date format.
	 */
	public static final String SIG_DATE_INVALID_MONTH =
			"invalid.prop.date.string.local.month";

	/**
	 * signature for validation exception because of wrong date format.
	 */
	public static final String SIG_DATE_INVALID_YEAR =
			"invalid.prop.date.string.local.year";

	/**
	 * validate an input field.
	 * 
	 * @return if the string in the input field is valid
	 *         or at least could at least get after appending additional
	 *         characters.
	 * 
	 * @param ex
	 *            the validation exception
	 */
	protected boolean hasPotentiallyValidInputField(
			final ValidationException ex) {
		if (ex.getSignature().endsWith("incomplete")) {
			return this.checkLocalDate(false);
		} else {
			return this.inputFieldValueCompleted;
		}
	}

	/**
	 * show if the input field valuehas been expanded.
	 */
	private boolean inputFieldValueCompleted = false;

	/**
	 * @return the inputFieldValueCompleted
	 */
	protected boolean isInputFieldValueCompleted() {
		return inputFieldValueCompleted;
	}

	/**
	 * check the local date.
	 * 
	 * @param completenessRequired
	 *            if completeness is required
	 * 
	 * @return if the local date is ok
	 */
	protected boolean checkLocalDate(final boolean completenessRequired) {
		boolean ok = true;
		try {
			parseLocalDate(completenessRequired);
		} catch (ValidationException e) {
			ok = false;
		}
		return ok;
	}

	/**
	 * A helper class for date parsing.
	 * 
	 * @author Martin Bluemel
	 */
	private class ParsedDate {

		/**
		 * the day.
		 */
		private int day = -1;

		/**
		 * the month.
		 */
		private int month = -1;

		/**
		 * the year.
		 */
		private int year = -1;

		/**
		 * the day.
		 */
		private String sday = null;

		/**
		 * the month.
		 */
		private String smonth = null;

		/**
		 * the year.
		 */
		private String syear = null;

		/**
		 * the ok flag.
		 */
		private boolean ok = false;

		/**
		 * the locale.
		 */
		private Locale locale = null;

		/**
		 * the date string.
		 */
		private String sDate = null;

		/**
		 * the flag that shows if completion has been performed.
		 */
		private boolean completed = false;

		/**
		 * getter.
		 * 
		 * @return the flag that shows if completion has been performed
		 */
		public boolean isCompleted() {
			return this.completed;
		}

		/**
		 * constructor.
		 * 
		 * @param s
		 *            the date string
		 * @param l
		 *            the locale
		 */
		ParsedDate(final String s, final Locale l) {
			this.sDate = s;
			this.locale = l;
		}

		/**
		 * @param s
		 *            the day String to set
		 */
		public void setDay(final String s) {
			this.sday = s;
			try {
				this.day = Integer.parseInt(s);
				if (s.length() < 2) {
					this.completed = true;
				}
			} catch (NumberFormatException e) {
				throw new ValidationException(SIG_DATE_INVALID_DAY,
						this,
						"Invalid day in localized date string \""
								+ this.sDate + "\"" + " for locale "
								+ this.locale.getDisplayName());
			}
		}

		/**
		 * @param s
		 *            the month to set
		 */
		public void setMonth(final String s) {
			this.smonth = s;
			try {
				this.month = Integer.parseInt(s);
				if (s.length() < 2) {
					this.completed = true;
				}
			} catch (NumberFormatException e) {
				throw new ValidationException(SIG_DATE_INVALID_MONTH,
						this,
						"Invalid month in localized date string \""
								+ this.sDate + "\"" + " for locale "
								+ this.locale.getDisplayName());
			}
		}

		//        /**
		//         * @param o the ok to set
		//         */
		//        public void setOk(final boolean o) {
		//            this.ok = o;
		//        }

		/**
		 * @param s
		 *            the year to set
		 */
		public void setYear(final String s) {
			this.syear = s;
			try {
				this.year = Integer.parseInt(s);
				if (this.year >= 0 && this.year <= 19) {
					this.year += 2000;
					this.syear = Integer.toString(this.year);
					this.sDate = this.sday + '.' + this.smonth + "." + this.syear;
				} else if (this.year >= 20 && this.year <= 99) {
					this.year += 1900;
					this.syear = Integer.toString(this.year);
					this.sDate = this.sday + '.' + this.smonth + "." + this.syear;
				} else if (this.year >= 100 && this.year <= 999) {
					if (this.syear.length() == 3) {
						this.syear = '0' + this.syear;
						this.sDate = this.sday + '.' + this.smonth + "." + this.syear;
					}
				}
				if (s.length() < 4) {
					this.completed = true;
				}
			} catch (NumberFormatException e) {
				throw new ValidationException(SIG_DATE_INVALID_YEAR,
						this,
						"Invalid year in localized date string \""
								+ this.sDate + "\"" + " for locale "
								+ this.locale.getDisplayName());
			}
		}

		/**
		 * conversion to a date.
		 * 
		 * @param precision
		 *            the precision for the date
		 * 
		 * @return the date
		 */
		public Date toDate(final PrecisionDate precision) {
			if (!this.ok) {
				throw new ValidationException(SIG_DATE_INVALID_FORMAT,
						this,
						"invalid localized medium date string \""
								+ this.sDate + "\" for locale "
								+ this.locale.getDisplayName());
			}
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(this.year, this.month - 1, this.day);
			return new Date(PropertyDate.cutPrecisionLong(
					cal.getTimeInMillis(), precision));
		}

		/**
		 * validate the date.
		 * 
		 * @param completenessRequired
		 *            if completeness is required
		 */
		public void validate(final boolean completenessRequired) {
			if (completenessRequired) {
				if (this.syear == null) {
					throw new ValidationException(SIG_DATE_INVALID_ICOMPLETE,
							this,
							"Incomplete localized medium date string \""
									+ this.sDate + "\"" + " for locale "
									+ this.locale.getDisplayName());
				}
			}

			if (this.sday != null) {
				if (this.smonth != null) {
					if (this.syear != null) { // (1,1,1)
						if (this.year < 0 || this.year > 9999) {
							throw new ValidationException(SIG_DATE_INVALID_YEAR,
									this,
									"Invalid year in localized date string \""
											+ this.sDate + "\"" + " for locale "
											+ this.locale.getDisplayName());
						}
						if (this.month == 2 && this.day == 29) {
							DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, this.locale);
							GregorianCalendar cal = new GregorianCalendar();
							cal.set(this.year, this.month - 1, this.day);
							String normFormat = df.format(cal.getTime());

							if (!(normFormat.equals(sDate))) {
								throw new ValidationException(SIG_DATE_INVALID_DAY,
										this,
										"Invalid day february 29th in localized date string \""
												+ this.sDate + "\"" + " for locale "
												+ this.locale.getDisplayName());
							}
						}
					}
					// (1,1,x)
					if (this.month == 0) {
						if (this.smonth.length() == 1) {
							if (completenessRequired) {
								throw new ValidationException(SIG_DATE_INVALID_MONTH,
										this,
										"Invalid month localized date string \""
												+ this.sDate + "\"" + " for locale "
												+ this.locale.getDisplayName());

							}
						} else {
							throw new ValidationException(SIG_DATE_INVALID_MONTH,
									this,
									"Invalid month in localized date string \""
											+ this.sDate + "\"" + " for locale "
											+ this.locale.getDisplayName());
						}
					}
					if (this.month < 0 || this.month > 12) {
						throw new ValidationException(SIG_DATE_INVALID_MONTH,
								this,
								"Invalid month in localized date string \""
										+ this.sDate + "\"" + " for locale "
										+ this.locale.getDisplayName());
					}
					switch (this.month) {
					case 1:
					case 3:
					case 5:
					case 7:
					case 8:
					case 10:
					case 12:
						if (this.day < 1 || this.day > 31) {
							throw new ValidationException(SIG_DATE_INVALID_DAY,
									this,
									"Invalid day in localized date string \""
											+ this.sDate + "\"" + " for locale "
											+ this.locale.getDisplayName());
						}
						break;
					case 4:
					case 6:
					case 9:
					case 11:
						if (this.day < 1 || this.day > 30) {
							throw new ValidationException(SIG_DATE_INVALID_DAY,
									this,
									"Invalid day in localized date string \""
											+ this.sDate + "\"" + " for locale "
											+ this.locale.getDisplayName());
						}
						break;
					case 2:
						if (this.day < 1 || this.day > 29) {
							throw new ValidationException(SIG_DATE_INVALID_DAY,
									this,
									"Invalid day in localized date string \""
											+ this.sDate + "\"" + " for locale "
											+ this.locale.getDisplayName());
						}
						break;
					default:
						break;
					}
				} else { //(1,0,0)
					if (this.day == 0) {
						if (this.sday.length() == 1) {
							if (completenessRequired) {
								throw new ValidationException(SIG_DATE_INVALID_DAY,
										this,
										"Invalid day in localized date string \""
												+ this.sDate + "\"" + " for locale "
												+ this.locale.getDisplayName());
							}
						} else {
							throw new ValidationException(SIG_DATE_INVALID_DAY,
									this,
									"Invalid day in localized date string \""
											+ this.sDate + "\"" + " for locale "
											+ this.locale.getDisplayName());
						}
					}
					if (this.day < 0 || this.day > 31) {
						throw new ValidationException(SIG_DATE_INVALID_DAY,
								this,
								"Invalid day in localized date string \""
										+ this.sDate + "\"" + " for locale "
										+ this.locale.getDisplayName());
					}
				}
			}
			this.ok = true;
		}
	}

	/**
	 * validate the date field.
	 * 
	 * @param completenessRequired
	 *            if the input fields must be complete
	 * 
	 * @return if the string in the date field is valid or at least could
	 *         at least get after appending additional characters.
	 */
	private ParsedDate parseLocalDate(final boolean completenessRequired) {
		ParsedDate date = new ParsedDate(this.text.getText(), this.getLocale().getLocale());
		if (this.getLocale().getName().equals("de")) {
			String s = this.text.getText();
			int len = s.length();
			char c;
			StringBuffer sb = new StringBuffer();
			int state = 0;
			int j = 0;
			for (int i = 0; i < len; i++) {
				c = s.charAt(i);
				switch (state) {
				case 0:
				case 1:
				case 2:
					if (c >= '0' && c <= '9') {
						if (((state == 2) && (i >= (j + 1) && i <= (j + 4)))
								|| ((state == 0) && (i >= 0 && i <= 1))
								|| ((state == 1) && (i >= (j + 1) && i <= (j + 2)))) {
							sb.append(c);
						} else {
							throw new ValidationException(SIG_DATE_INVALID_FORMAT,
									this,
									"invalid localized medium date string \"" + s + "\""
											+ " for locale " + this.getLocale().getLocale().getDisplayName());
						}
					} else if ((state == 0 || state == 1) && (c == '.')) {
						j = i;
						switch (state) {
						case 0:
							date.setDay(sb.toString());
							break;
						case 1:
							date.setMonth(sb.toString());
							break;
						default:
							throw new RapidBeansRuntimeException("Error during parsing a date");
						}
						sb.setLength(0);
						state++;
					} else {
						throw new ValidationException(SIG_DATE_INVALID_FORMAT,
								this,
								"invalid localized medium date string \"" + s + "\""
										+ " for locale " + this.getLocale().getLocale().getDisplayName());
					}
					break;
				default:
					throw new RapidBeansRuntimeException("unexcpected state " + state);
				}
			}
			if (sb.length() > 0) {
				switch (state) {
				case 0:
					date.setDay(sb.toString());
					break;
				case 1:
					date.setMonth(sb.toString());
					break;
				case 2:
					date.setYear(sb.toString());
					break;
				default:
					throw new RapidBeansRuntimeException("Error during parsing a date");
				}
			}

			date.validate(completenessRequired);
		}
		return date;
	}

	/**
	 * @return the input field value as string.
	 */
	public String getInputFieldValueString() {
		return this.text.getText();
	}
}
