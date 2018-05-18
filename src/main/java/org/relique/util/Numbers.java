package org.relique.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Numbers {
	public static int compare(Number x, Number y) {
	    if(isSpecial(x) || isSpecial(y))
	        return Double.compare(x.doubleValue(), y.doubleValue());
	    else
	        return toBigDecimal(x).compareTo(toBigDecimal(y));
	}

	private static boolean isSpecial(Number x) {
	    boolean specialDouble = x instanceof Double
	            && (Double.isNaN((Double) x) || Double.isInfinite((Double) x));
	    boolean specialFloat = x instanceof Float
	            && (Float.isNaN((Float) x) || Float.isInfinite((Float) x));
	    return specialDouble || specialFloat;
	}

	private static BigDecimal toBigDecimal(Number number) {
	    if(number instanceof BigDecimal)
	        return (BigDecimal) number;
	    if(number instanceof BigInteger)
	        return new BigDecimal((BigInteger) number);
	    if(number instanceof Byte || number instanceof Short
	            || number instanceof Integer || number instanceof Long)
	        return new BigDecimal(number.longValue());
	    if(number instanceof Float || number instanceof Double)
	        return new BigDecimal(number.doubleValue());

	    try {
	        return new BigDecimal(number.toString());
	    } catch(final NumberFormatException e) {
	        throw new RuntimeException("The given number (\"" + number + "\" of class " + number.getClass().getName() + ") does not have a parsable string representation", e);
	    }
	}

}
