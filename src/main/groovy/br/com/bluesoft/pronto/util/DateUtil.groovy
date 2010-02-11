package br.com.bluesoft.pronto.util

import java.sql.Timestamp;
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

public class DateUtil {

	private static final String PADRAO_COMPLETO = "dd/MM/yyyy"
	private static final String PADRAO_MES_ANO = "dd/MM"
	private static final String PADRAO_HORA = "HH:mm"

	public static String toString(final Date date) {
		if (date != null) {
			return new SimpleDateFormat(PADRAO_COMPLETO).format(date)
		} else {
			return null
		}
	}

	public static String toStringMesAno(final Date date) {
		return new SimpleDateFormat(PADRAO_MES_ANO).format(date)
	}

	public static String toStringHora(final Date date) {
		return new SimpleDateFormat(PADRAO_HORA).format(date)
	}

	public static Date add(final Date date, final int days) {
		final Calendar calendar = Calendar.getInstance()
		calendar.setTime(date)
		calendar.add(Calendar.DAY_OF_MONTH, days)
		return calendar.getTime()
	}
	
	
	
	
	public static getTimestampSemMilissegundos(Timestamp timestamp) {
		if (timestamp == null) {
			return null
		}
		
		def cal = Calendar.getInstance()
		cal.setTime timestamp
		cal.set Calendar.MILLISECOND, 0
		return new Timestamp(cal.getTime().getTime())
	}
	
}
