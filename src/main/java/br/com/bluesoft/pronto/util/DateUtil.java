package br.com.bluesoft.pronto.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static final String PADRAO = "dd/MM/yyyy";

	public static String toString(final Date date) {
		return new SimpleDateFormat(PADRAO).format(date);
	}

	public static Date add(final Date date, final int days) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}

}
