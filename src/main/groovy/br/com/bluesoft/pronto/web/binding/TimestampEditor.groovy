package br.com.bluesoft.pronto.web.binding

import java.sql.Timestamp
import java.text.DateFormat
import java.text.ParseException

import org.springframework.util.StringUtils

class TimestampEditor extends java.beans.PropertyEditorSupport {

	boolean allowEmpty = false
	DateFormat dateFormat

	TimestampEditor(final DateFormat dateFormat, boolean allowEmpty) {
		this.allowEmpty = allowEmpty
		this.dateFormat = dateFormat
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		java.util.Date d = null
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			setValue(null)
		} else {
			try {
				d = dateFormat.parse(text)
				setValue(new Timestamp(d.getTime()))
			} catch (final ParseException ex) {
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage())
			}
		}
	}

	@Override
	public String getAsText() {
		final Date value = (java.sql.Date) getValue()
		if (value != null) {
			final java.util.Date d = new java.util.Date(value.getTime())
			return dateFormat.format(d)

		} else {
			return ""
		}
	}
}
