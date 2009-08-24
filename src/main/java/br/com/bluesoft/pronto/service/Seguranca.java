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

package br.com.bluesoft.pronto.service;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import br.com.bluesoft.pronto.SegurancaException;
import br.com.bluesoft.pronto.model.Usuario;

@Service
public class Seguranca {

	private static final ThreadLocal<Usuario> usuarios = new ThreadLocal<Usuario>();
	private static final Usuario anonimo;

	static {
		anonimo = new Usuario();
		anonimo.setUsername("anonimo");
	}

	public static void setUsuario(final Usuario usuario) {
		usuarios.set(usuario);
	}

	public static Usuario getUsuario() {
		if (usuarios.get() == null) {
			return anonimo;
		} else {
			return usuarios.get();
		}
	}

	public static void removeUsuario() {
		usuarios.remove();
	}

	public static String encrypt(final String x) {
		try {
			final MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(x.getBytes());
			final byte[] hashMd5 = md.digest();
			final byte[] base64 = Base64.encodeBase64(hashMd5);
			return new String(base64);
		} catch (final Exception e) {
			return null;
		}
	}

	public static void validarPermissao(final int... papeis) throws SegurancaException {

		boolean permitido = false;

		for (final int papelKey : papeis) {
			permitido = permitido || getUsuario().temOPapel(papelKey);
		}

		if (!permitido) {
			throw new SegurancaException("Você não possui permissão para realizar essa operação.");
		}

	}

}
