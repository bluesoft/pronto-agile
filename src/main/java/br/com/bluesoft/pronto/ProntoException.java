package br.com.bluesoft.pronto;

public class ProntoException extends Exception {

	private static final long serialVersionUID = 1L;

	public ProntoException() {

	}

	public ProntoException(final String m) {
		super(m);
	}

	public ProntoException(final String m, final Throwable t) {
		super(m, t);
	}

}
