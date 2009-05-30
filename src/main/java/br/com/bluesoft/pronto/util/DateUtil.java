package br.com.bluesoft.pronto.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private static final String PADRAO = "dd/MM/yyyy"; 
	
	public static String toString(final Date date) {
		return new SimpleDateFormat(PADRAO).format(date);
	}
	
}
