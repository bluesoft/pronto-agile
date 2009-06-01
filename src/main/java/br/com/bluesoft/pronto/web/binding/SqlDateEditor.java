package br.com.bluesoft.pronto.web.binding;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;

import org.springframework.util.StringUtils;

public class SqlDateEditor extends java.beans.PropertyEditorSupport {

	private boolean allowEmpty = false;
	private final DateFormat dateFormat;

	public SqlDateEditor(final DateFormat dateFormat, final boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
		this.dateFormat = dateFormat;
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		java.util.Date d = null;
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			setValue(null);
		} else {
			try {
				d = dateFormat.parse(text);
				setValue(new Date(d.getTime()));
			} catch (final ParseException ex) {
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage());
			}
		}
	}

	@Override
	public String getAsText() {
		final Date value = (java.sql.Date) getValue();
		if (value != null) {
			final java.util.Date d = new java.util.Date(value.getTime());
			return dateFormat.format(d);

		} else {
			return "";
		}
	}
}
