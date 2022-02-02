/*
 * Rapid Beans Framework: PropertyChoice.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/22/2005
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

package org.rapidbeans.core.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyChoice;
import org.rapidbeans.core.type.TypeRapidEnum;

/**
 * A <b>Choice</b> bean property encapsulates a collection of EasyBiz enum
 * elements.<br/>
 * Attributes<br/>
 * <b>enum: (mandatory)</b> specifies the bean enum type (class)<br/>
 * <b>multiple: {'false' | 'true'}, default = 'false'</b> specifies if the
 * choice is single or multiple<br/>
 * <b>default: default = null</b> specifies the default value (comma separated
 * list of enum element names)<br/>
 * 
 * @author Martin Bluemel
 */
public class PropertyChoice extends Property {

	/**
	 * the Choice value which is a collection of the chosen enum elements. !!! do
	 * not initialize here because the superclass does it with the property type's
	 * default value
	 */
	private ArrayList<RapidEnum> value;

	/**
	 * constructor for a new Choice Property.
	 * 
	 * @param type       the Property's type
	 * @param parentBean the parent bean
	 */
	public PropertyChoice(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}

	/**
	 * generic value getter.
	 * 
	 * @return the value of this Property as java.util.List&lt;RapidEnum&gt;
	 *         containing all enum elements that are members of this choice
	 */
	@SuppressWarnings("unchecked")
	public ReadonlyListCollection<?> getValue() {
		ReadonlyListCollection<RapidEnum> value = null;
		if (getBean() instanceof RapidBeanImplSimple) {
			final Object refValue = Property.getValueFieldByReflection(getBean(), getName());
			if (refValue == null) {
				return null;
			} else if (refValue instanceof RapidEnum) {
				value = new ReadonlyListCollection<RapidEnum>(Arrays.asList(new RapidEnum[] { (RapidEnum) refValue }),
						this.getType());
			} else {
				value = new ReadonlyListCollection<RapidEnum>((List<RapidEnum>) refValue, this.getType());
			}
		} else {
			if (this.value != null) {
				// we encapsulate the collection to keep the property immutable
				value = new ReadonlyListCollection<RapidEnum>(this.value, this.getType());
			}
		}
		return value;
	}

	/**
	 * String value getter.
	 * 
	 * @return the String representation of the Property's value.<br/>
	 *         For a Choice this is a comma separated list of enum element names. In
	 *         case of a non multiple Choice of course it is only one enum element
	 *         name.
	 */
	public String toString() {
		final ReadonlyListCollection<?> value = getValue();
		if (value == null) {
			return null;
		}
		switch (value.size()) {
		case 0:
			return ("");
		case 1:
			return (value.get(0).toString());
		default:
			StringBuffer sb = new StringBuffer(value.get(0).toString());
			int size = value.size();
			for (int i = 1; i < size; i++) {
				sb.append(",");
				sb.append(value.get(i).toString());
			}
			return sb.toString();
		}
	}

	/**
	 * generic value setter.
	 * 
	 * @param newValue the new value for this property.<br/>
	 *                 Must be an instance of the following classes:<br/>
	 *                 <b>Collection&lt;RapidEnum&gt;:</b> any collection of enum
	 *                 elements<br/>
	 *                 <b>RapidEnum:</b> a single enum element<br/>
	 *                 <b>String:</b> a comma separated list of enum element
	 *                 names<br/>
	 *                 <b>String[]:</b> a string array of enum element names
	 */
	public void setValue(final Object newValue) {
		super.setValueWithEvents(this.value, newValue, new PropertyValueSetter() {
			@SuppressWarnings("unchecked")
			public void setValue(final Object newValue) {
				if (getBean() instanceof RapidBeanImplSimple) {
					if (((TypePropertyChoice) getType()).getMultiple()) {
						Property.setValueByReflection(getBean(), getName(), newValue);
					} else {
						if (newValue == null) {
							Property.setValueByReflection(getBean(), getName(), null);
						} else {
							Property.setValueByReflection(getBean(), getName(), ((List<RapidEnum>) newValue).get(0));
						}
					}
				} else {
					value = (ArrayList<RapidEnum>) newValue;
				}
			}
		});
	}

	/**
	 * Converts different classes to the Property's internal value class.<br/>
	 * 
	 * @param choiceValue the value to convert<br/>
	 *                    Must be an instance of the following classes:
	 *                    <li><b>Collection&lt;RapidEnum&gt;:</b> any collection of
	 *                    enum elements</li>
	 *                    <li><b>RapidEnum:</b> a single enum element</li>
	 *                    <li><b>String:</b> a comma separated list of enum element
	 *                    names</li>
	 *                    <li><b>String[]:</b> a string array of enum element
	 *                    names</li>
	 * 
	 * @return an ArrayList of enum elements
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<RapidEnum> convertValue(final Object choiceValue) {

		ArrayList<RapidEnum> choice = null;

		if (choiceValue != null) {
			if (choiceValue instanceof Collection) {
				final List<RapidEnum> argValList = (List<RapidEnum>) choiceValue;
				choice = new ArrayList<RapidEnum>();
				for (RapidEnum rapidEnum : argValList) {
					choice.add(rapidEnum);
				}
			} else if (choiceValue instanceof RapidEnum) {
				choice = new ArrayList<RapidEnum>();
				choice.add((RapidEnum) choiceValue);
			} else if (choiceValue instanceof String) {
				choice = ((TypePropertyChoice) this.getType()).getEnumType().parse((String) choiceValue);
			} else if (choiceValue instanceof String[]) {
				final String[] sa = (String[]) choiceValue;
				choice = new ArrayList<RapidEnum>();
				TypeRapidEnum enumType = ((TypePropertyChoice) this.getType()).getEnumType();
				for (int i = 0; i < sa.length; i++) {
					choice.add(enumType.elementOf(sa[i]));
				}
			} else {
				throw new ValidationException("invalid.prop.choice.type", this,
						"Choice property \"" + this.getType().getPropName() + "\": " + " invalid data type "
								+ choiceValue.getClass().getName()
								+ ".\nOnly \"RapidEnum[]\", or \"String[]\", or \"String\" are valid data types.");
			}
		}

		return choice;
	}

	/**
	 * generic validation for the Property's value.
	 * 
	 * @param newValue the value to validate<br/>
	 *                 Must be an instance of the following classes:<br/>
	 *                 <b>Collection&lt;RapidEnum&gt;:</b> any collection of enum
	 *                 elements<br/>
	 *                 <b>RapidEnum:</b> a single enum element<br/>
	 *                 <b>String:</b> a comma separated list of enum element
	 *                 names<br/>
	 *                 <b>String[]:</b> a string array of enum element names
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<RapidEnum> validate(final Object newValue) {
		final ArrayList<RapidEnum> newChoiceValue = (ArrayList<RapidEnum>) super.validate(newValue);
		if (!ThreadLocalValidationSettings.getValidation()) {
			return newChoiceValue;
		}
		if (newValue == null) {
			return null;
		}
		final TypePropertyChoice type = (TypePropertyChoice) this.getType();
		final int size = newChoiceValue.size();
		if (!type.getMultiple() && newChoiceValue.size() > 1) {
			throw new ValidationException("invalid.prop.choice.more", this, "Property \"" + this.getType().getPropName()
					+ "\": " + " invalid multiple choice." + "\nMore than one item chosen in a non multiple choice");
		}
		if (newChoiceValue != null) {
			int j;
			for (int i = 0; i < size; i++) {
				final RapidEnum newEnumElement = newChoiceValue.get(i);
				if (newEnumElement == null) {
					throw new ValidationException("invalid.prop.choice.null", this,
							"Invalid  value \"" + value.toString() + "\" for bean instance \""
									+ getBean().toStringGui(null) + "\"\n Property \"" + this.getType().getPropName()
									+ "\": " + " null enum is not allowed");
				}
				if (!newEnumElement.getType().isAssignableFrom(((TypePropertyChoice) getType()).getEnumType())) {
					throw new ValidationException("invalid.prop.choice.valuetype", this,
							"Property \"" + this.getType().getPropName() + "\": " + " value type \""
									+ newEnumElement.getType().getName() + "\" is not allowed\n"
									+ "  Allowd value type: \""
									+ ((TypePropertyChoice) getType()).getEnumType().getName() + "\"",
							new Object[] { newEnumElement.getType().getName(),
									((TypePropertyChoice) getType()).getEnumType().getName() });
				}
				for (j = i + 1; j < size; j++) {
					if (newEnumElement == newChoiceValue.get(j)) {
						if (this.getBean() != null) {
							throw new ValidationException("invalid.prop.choice.duplicate", this,
									"Bean \"" + this.getBean().getType().getName() + "::" + this.getBean().toString()
											+ ", " + "Property \"" + this.getType().getPropName() + "\": "
											+ " invalid duplicate choice." + "\nOne item was chosen more than once.");
						} else {
							throw new ValidationException("invalid.prop.choice.duplicate", this,
									"Property \"" + this.getType().getPropName() + "\": " + " invalid duplicate choice."
											+ "\nOne item was chosen more than once.");
						}
					}
				}
			}
		}
		return newChoiceValue;
	}

	/**
	 * @param locale the Locale
	 * @return a string for the property's value for UI
	 */
	public String toStringGui(final RapidBeansLocale locale) {
		int i = 0;
		StringBuffer sb = new StringBuffer();
		if (this.value != null) {
			for (RapidEnum entry : this.value) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(entry.toStringGui(locale));
				i++;
			}
		}
		return sb.toString();
	}
}
