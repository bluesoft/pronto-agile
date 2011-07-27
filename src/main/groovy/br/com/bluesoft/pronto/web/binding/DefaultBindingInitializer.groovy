package br.com.bluesoft.pronto.web.binding;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * Aqui sao definidas as regras de Binding que sao aplicadas por padrao as todos os controllers do SpringMVC.
 */
public class DefaultBindingInitializer implements WebBindingInitializer {

	@Override
	public void initBinder(final WebDataBinder binder, final WebRequest webRequest) {
		registrarBinderParaTimestamp(binder);
		registrarBinderParaDatas(binder);
		registrarBinderParaNumeros(binder);
	}

	private void registrarBinderParaNumeros(final WebDataBinder binder) {
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, NumberFormat.getInstance(), true));
	}

	private void registrarBinderParaDatas(final WebDataBinder binder) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(java.sql.Date.class, new SqlDateEditor(dateFormat, true));
	}
	
	private void registrarBinderParaTimestamp(final WebDataBinder binder) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(java.sql.Timestamp.class, new TimestampEditor(dateFormat, true));
	}

}
