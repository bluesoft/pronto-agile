package br.com.bluesoft.pronto.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	public static String hex(final byte[] array) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString(array[i] & 0xFF | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

	public static String md5Hex(final String message) {
		try {
			final MessageDigest md = MessageDigest.getInstance("MD5");
			return hex(md.digest(message.getBytes("CP1252")));
		} catch (final NoSuchAlgorithmException e) {
		} catch (final UnsupportedEncodingException e) {
		}
		return null;
	}
}
