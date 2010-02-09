/*
 * Copyright 2009 Pronto Agile Project Management.
 *
 * This file is part of Pronto.
 *
 * Pronto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pronto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package br.com.bluesoft.pronto.web.binding;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * Aqui são definidas as regras de Binding que são aplicadas por padrão as todos os controllers do SpringMVC.
 * @author André Faria
 */
public class DefaultBindingInitializer implements WebBindingInitializer {

	@Override
	public void initBinder(final WebDataBinder binder, final WebRequest webRequest) {
		registrarBinderParaDatas(binder);
		registrarBinderParaNumeros(binder);
	}

	private void registrarBinderParaNumeros(final WebDataBinder binder) {
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, NumberFormat.getInstance(), true));
	}

	private void registrarBinderParaDatas(final WebDataBinder binder) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(java.sql.Date.class, new SqlDateEditor(dateFormat, true));
	}

}
