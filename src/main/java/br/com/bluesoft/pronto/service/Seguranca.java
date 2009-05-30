package br.com.bluesoft.pronto.service;

import org.springframework.stereotype.Service;

import br.com.bluesoft.pronto.model.Usuario;

@Service
public class Seguranca {

	private static ThreadLocal<Usuario> usuarios = new ThreadLocal<Usuario>();

	public static void setUsuario(Usuario usuario) {
		usuarios.set(usuario);
	}

	public static Usuario getUsuario() {
		return usuarios.get();
	}

	public static void removeUsuario() {
		usuarios.remove();

	}
}
