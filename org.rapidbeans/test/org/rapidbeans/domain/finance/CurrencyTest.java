/*
 * Rapid Beans Framework: LengthTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Nov 14, 2005
 */
package org.rapidbeans.domain.finance;

import java.util.HashMap;
import java.util.Locale;

import junit.framework.TestCase;

/**
 * Unit tests for class Length.
 * 
 * @author Martin Bluemel
 */
public final class CurrencyTest extends TestCase {

	/**
	 * test get Order.
	 */
	public void testGetOrder() {
		Currency euro = Currency.euro;
		assertEquals(1, euro.ordinal());
		Currency dm = Currency.dollar;
		assertEquals(0, dm.ordinal());
	}

	/**
	 * test get Name.
	 */
	public void testGetName() {
		Currency euro = Currency.euro;
		assertEquals("euro", euro.name());
		Currency dm = Currency.dollar;
		assertEquals("dollar", dm.name());
	}

	/**
	 * test java currency.
	 */
	public void testJavaCurrency() {

		java.util.Currency cur = java.util.Currency.getInstance(Locale.GERMANY);
		assertEquals("EUR", cur.getCurrencyCode());
		// "\u20AC" is the Unicode for the Euro symbol '€'
		assertTrue(cur.getSymbol().equals("\u20AC") || cur.getSymbol().equals("EUR"));

		final HashMap<String, java.util.Currency> currencyMap = new HashMap<String, java.util.Currency>();
		for (String isoLanguage : Locale.getISOLanguages()) {
			for (String isoCountry : Locale.getISOCountries()) {
				final Locale locale = new Locale(isoLanguage, isoCountry);
				if (locale != null) {
					final java.util.Currency currency = java.util.Currency.getInstance(locale);
					if (currency != null) {
						if (!currencyMap.containsKey(currency.getCurrencyCode())) {
							currencyMap.put(currency.getCurrencyCode(), currency);
						}
					}
				}
			}
		}
		// final String[] currencyCodes = new
		// String[currencyMap.keySet().size()];
		// int i = 0;
		// for (final Object o : currencyMap.keySet()) {
		// currencyCodes[i++] = (String) o;
		// }
		// Arrays.sort(currencyCodes);
		// for (final String currencyCode : currencyCodes) {
		// final java.util.Currency currency = (java.util.Currency)
		// currencyMap.get(currencyCode);
		// System.out.println("Currency: " + currency.getCurrencyCode()
		// + ", Symbol: " + currency.getSymbol(Locale.GERMANY));
		// }
	}
}
