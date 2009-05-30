package br.com.bluesoft.pronto.service;

import java.security.MessageDigest;

import org.springframework.stereotype.Service;

import br.com.bluesoft.pronto.model.Usuario;

@Service
public class Seguranca {

	private static final ThreadLocal<Usuario> usuarios = new ThreadLocal<Usuario>();
	private static final Usuario anonimo;
	static {
		anonimo = new Usuario();
		anonimo.setUsername("anonimo");
	}

	public static void setUsuario(Usuario usuario) {
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

	public String md5(String x) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(x.getBytes());
			byte[] hashMd5 = md.digest();
			return new String(hashMd5);
		} catch (Exception e) {
			return null;
		}
	}
}
