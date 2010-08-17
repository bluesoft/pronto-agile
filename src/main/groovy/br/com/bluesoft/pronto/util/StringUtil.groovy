package br.com.bluesoft.pronto.util

class StringUtil {
	
	static String retiraAcentuacao(String input) {
		
		if (input == null || input.length() < 1) {
			return input
		}
		
		String out = input
		final def comAcento = [ 'ç', 'á', 'à', 'ã', 'â', 'ä', 'é', 'è', 'ê', 'ë', 'í', 'ì', 'î', 'ï', 'ó', 'ò', 'õ', 'ô', 'ö', 'ú', 'ù', 'û', 'ü', 'Â' ]
		final def semAcento = [ 'c', 'a', 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'e', 'i', 'i', 'i', 'i', 'o', 'o', 'o', 'o', 'o', 'u', 'u', 'u', 'u', 'A' ]
		
		for (int i = 0; i < comAcento.size(); i++) {
			out = out.replace(comAcento[i].toLowerCase(), semAcento[i].toLowerCase())
			out = out.replace(comAcento[i].toUpperCase(), semAcento[i].toUpperCase())
		}
		return out
	}
	
}
