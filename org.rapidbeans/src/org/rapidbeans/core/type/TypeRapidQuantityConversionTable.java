/*
 * Rapid Beans Framework: TypeRapidQuantityConversionTable.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/14/2005
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

package org.rapidbeans.core.type;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;

/**
 * A helper class for converting quanitiest.
 * 
 * @author Martin Bluemel
 */
public final class TypeRapidQuantityConversionTable {

	private static final Logger log = Logger
			.getLogger(TypeRapidQuantityConversionTable.class.getName());

	/**
	 * the "normal" unit with conversion factor 1
	 */
	private RapidEnum normUnit = null;

	/**
	 * specifies the quantitie's units.
	 */
	private RapidEnum[] units = null;

	/**
	 * the table containing all conversion factors.
	 */
	private BigDecimal[][] conversionFactorTable = null;

	/**
	 * the table containing reciprocal flags for all conversion factors.
	 */
	private boolean[][] conversionFactorTableReciprocalFlag = null;

	/**
	 * computing strategy.
	 */
	private MathContext mathContext = MathContext.DECIMAL32;

	/**
	 * constructor for a conversion conversionFactorTable.
	 * 
	 * @param argUnitType
	 *            the enum type wich is the quantity type's unit.
	 * @param argDescr
	 *            the String containing the conversion factors of each unit.
	 *            There are two different formats accepted to describe the
	 *            factors:<br/>
	 *            1) XML: 2) Proprietary:
	 */
	public TypeRapidQuantityConversionTable(final TypeRapidEnum argUnitType,
			final String argDescr) {
		log.fine("parsing \"" + argDescr + "\"");
		List<RapidEnum> enums = argUnitType.getElements();
		this.units = new RapidEnum[enums.size()];
		int i;
		for (i = 0; i < this.units.length; i++) {
			this.units[i] = enums.get(i);
		}
		this.conversionFactorTable = new BigDecimal[this.units.length + 1][this.units.length];
		this.conversionFactorTableReciprocalFlag = new boolean[this.units.length + 1][this.units.length];

		// fill the factor column
		int j;
		StringTokenizer st = new StringTokenizer(argDescr, ",");
		String[] sa = new String[st.countTokens()];
		StringBuffer sbUnitName = new StringBuffer();
		StringBuffer sbFactor = new StringBuffer();
		int state, len;
		String s;
		String sFactor;
		String unitName = null;
		RapidEnum unit = null;
		BigDecimal factor;
		char c, sign;
		for (i = 0; i < sa.length; i++) {
			s = st.nextToken();
			len = s.length();
			state = 0;
			sign = '?';
			for (j = 0; j < len; j++) {
				c = s.charAt(j);
				switch (state) {
				case 0:
					switch (c) {
					case '/':
					case '*':
						unitName = sbUnitName.toString();
						sbUnitName.setLength(0);
						unit = argUnitType.elementOf(unitName);
						sign = c;
						state = 1;
						break;
					default:
						sbUnitName.append(c);
						break;
					}
					break;

				case 1:
					sbFactor.append(c);
					break;
				default:
					break;
				}
			}
			sFactor = sbFactor.toString();
			sbFactor.setLength(0);
			factor = new BigDecimal(sFactor);
			if (sign == '/') {
				// instead of storing the reciprocal value here
				// divide instead of always multiplying later
				// this avoids numerical failure
				// factor = BigDecimal.ONE.divide(factor);
				this.conversionFactorTableReciprocalFlag[this.units.length][unit
						.ordinal()] = true;
			} else {
				this.conversionFactorTableReciprocalFlag[this.units.length][unit
						.ordinal()] = false;
			}

			// enter normal conversion
			this.conversionFactorTable[this.units.length][unit.ordinal()] = factor;
			log.fine("Normal Factor[" + unit + "," + this.units.length + ","
					+ unit.ordinal() + "] = " + factor);
			log.fine("      recip = ["
					+ unit
					+ ","
					+ this.units.length
					+ ","
					+ unit.ordinal()
					+ "] = "
					+ this.conversionFactorTableReciprocalFlag[this.units.length][unit
							.ordinal()]);

			if (this.normUnit == null && factor.equals(BigDecimal.ONE)) {
				this.normUnit = unit;
			}
		}

		// fill the conversion matrix out of the factor column
		BigDecimal f1, f2;
		boolean r1, r2;
		for (i = 0; i < this.units.length; i++) {
			f1 = this.conversionFactorTable[this.units.length][i];
			r1 = this.conversionFactorTableReciprocalFlag[this.units.length][i];
			for (j = i + 1; j < this.units.length; j++) {
				f2 = this.conversionFactorTable[this.units.length][j];
				r2 = this.conversionFactorTableReciprocalFlag[this.units.length][j];
				if (f1 != null && f2 != null) {
					if ((!r1) && (!r2)) {
						switch (f1.compareTo(f2)) {
						case 1:
							this.conversionFactorTable[j][i] = f1.divide(f2,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[j][i] = true;
							this.conversionFactorTable[i][j] = f1.divide(f2,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[i][j] = false;
							break;
						case -1:
							this.conversionFactorTable[j][i] = f2.divide(f1,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[j][i] = false;
							this.conversionFactorTable[i][j] = f2.divide(f1,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[i][j] = true;
							break;
						case 0:
							this.conversionFactorTable[j][i] = BigDecimal.ONE;
							this.conversionFactorTableReciprocalFlag[j][i] = false;
							this.conversionFactorTable[i][j] = BigDecimal.ONE;
							this.conversionFactorTableReciprocalFlag[i][j] = false;
							break;
						default:
							throw new RapidBeansRuntimeException(
									"compare unexpectedly deliverd value "
											+ f1.compareTo(f2));
						}
					} else if ((!r1) && r2) {
						if (f1.equals(BigDecimal.ONE.divide(f2))) {
							this.conversionFactorTable[j][i] = BigDecimal.ONE;
							this.conversionFactorTableReciprocalFlag[j][i] = false;
							this.conversionFactorTable[i][j] = BigDecimal.ONE;
							this.conversionFactorTableReciprocalFlag[i][j] = false;
						} else {
							this.conversionFactorTable[j][i] = f1.multiply(f2,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[j][i] = true;
							this.conversionFactorTable[i][j] = f1.multiply(f2,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[i][j] = false;
						}
					} else if (r1 && (!r2)) {
						if (f2.equals(BigDecimal.ONE.divide(f1))) {
							this.conversionFactorTable[j][i] = BigDecimal.ONE;
							this.conversionFactorTableReciprocalFlag[j][i] = false;
							this.conversionFactorTable[i][j] = BigDecimal.ONE;
							this.conversionFactorTableReciprocalFlag[i][j] = false;
						} else {
							this.conversionFactorTable[j][i] = f1.multiply(f2,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[j][i] = false;
							this.conversionFactorTable[i][j] = f1.multiply(f2,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[i][j] = true;
						}
					} else { // (r1 && r2)
						switch (f1.compareTo(f2)) {
						case 1:
							this.conversionFactorTable[j][i] = f1.divide(f2,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[j][i] = false;
							this.conversionFactorTable[i][j] = f1.divide(f2,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[i][j] = true;
							break;
						case -1:
							this.conversionFactorTable[j][i] = f2.divide(f1,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[j][i] = true;
							this.conversionFactorTable[i][j] = f2.divide(f1,
									this.mathContext);
							this.conversionFactorTableReciprocalFlag[i][j] = false;
							break;
						case 0:
							this.conversionFactorTable[j][i] = BigDecimal.ONE;
							this.conversionFactorTableReciprocalFlag[j][i] = false;
							this.conversionFactorTable[i][j] = BigDecimal.ONE;
							this.conversionFactorTableReciprocalFlag[i][j] = false;
							break;
						default:
							throw new RapidBeansRuntimeException(
									"compare unexpectedly deliverd value "
											+ f1.compareTo(f2));
						}
					}
				} else {
					this.conversionFactorTable[j][i] = null;
					this.conversionFactorTable[j][i] = null;
				}

				if (log.getLevel() == Level.FINE
						|| log.getLevel() == Level.FINEST) {
					String sRecip = "*";
					if (this.conversionFactorTableReciprocalFlag[i][j]) {
						sRecip = "/";
					}
					log.fine("factor [" + i + "][" + j + "] = " + sRecip
							+ this.conversionFactorTable[j][i]);
					sRecip = "*";
					if (this.conversionFactorTableReciprocalFlag[j][i]) {
						sRecip = "/";
					}
					log.fine("factor [" + j + "][" + i + "] = " + sRecip
							+ this.conversionFactorTable[i][j]);
				}
			}
		}
	}

	/**
	 * retrieve a certain conversion factor.
	 * 
	 * @param fromUnit
	 *            the source unit of the conversion
	 * @param toUnit
	 *            the target unit of the conversion
	 * @return the conversion factor
	 */
	public BigDecimal getConversionFactor(final RapidEnum fromUnit,
			final RapidEnum toUnit) {
		return this.conversionFactorTable[fromUnit.ordinal()][toUnit.ordinal()];
	}

	/**
	 * retrieve a certain conversion factor reciprocal state.
	 * 
	 * @param fromUnit
	 *            the source unit of the conversion
	 * @param toUnit
	 *            the target unit of the conversion
	 * @return if the conversion factor is a reciprocal value
	 */
	public boolean getConversionFactorReciprocalFlag(final RapidEnum fromUnit,
			final RapidEnum toUnit) {
		return this.conversionFactorTableReciprocalFlag[fromUnit.ordinal()][toUnit
				.ordinal()];
	}

	public void setConversionFactor(final RapidEnum fromUnit,
			final RapidEnum toUnit, final BigDecimal factor) {
		this.conversionFactorTable[fromUnit.ordinal()][toUnit.ordinal()] = factor;
		this.conversionFactorTableReciprocalFlag[fromUnit.ordinal()][toUnit
				.ordinal()] = false;
		this.conversionFactorTable[toUnit.ordinal()][fromUnit.ordinal()] = factor;
		this.conversionFactorTableReciprocalFlag[toUnit.ordinal()][fromUnit
				.ordinal()] = true;
	}

	/**
	 * for tracing and testing purposes.
	 */
	protected void dump() {
		System.out.print("unitenum name=\"" + this.units[0].getType().getName()
				+ "\"<");
		int i, j;
		for (i = 0; i < this.units.length; i++) {
			if (i == 0) {
				System.out.print(this.units[i].name());
			} else {
				System.out.print("," + this.units[i].name());
			}
		}
		System.out.println(">");
		for (i = 0; i < this.units.length; i++) {
			for (j = 0; j < this.units.length; j++) {
				if (j == 0) {
					System.out.print(this.conversionFactorTable[i][j]);
				} else {
					System.out.print("," + this.conversionFactorTable[i][j]);
				}
			}
			System.out.println();
		}
	}

	/**
	 * @return the normUnit
	 */
	public RapidEnum getNormUnit() {
		return normUnit;
	}
}
