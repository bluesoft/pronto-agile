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
