/**
 * 
 */
package de.engelhardt.app;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * 
 */
public class Utils {

	public static String toDecimal(Double in) {
		DecimalFormat formatter = new DecimalFormat("#,##0.00");
		formatter.setCurrency(Currency.getInstance(Locale.GERMANY));
		return formatter.format(in);
	}
}
