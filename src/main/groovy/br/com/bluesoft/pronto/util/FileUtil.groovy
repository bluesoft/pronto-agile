package br.com.bluesoft.pronto.util

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;

import antlr.StringUtils;
import br.com.bluesoft.pronto.model.Anexo;
import br.com.bluesoft.pronto.service.Config;

class FileUtil {
	
	static List<Anexo> listarAnexos( String folderPath) {
		File folder = new File(Config.getImagesFolder() + folderPath)
		folder.mkdirs()
		
		if (folder.exists()) {
			List<Anexo> anexos = new ArrayList<Anexo>()
			String[] files = folder.list()
			Arrays.sort(files)
			for ( String file : files) {
				anexos.add(new Anexo(file))
			}
			return anexos
		} else {
			return null
		}
	}
	
	static String getFileExtension( String nomeDoArquivo) {
		String extensao = null
		if (ehUmNomeDeArquivoValido(nomeDoArquivo)) {
			extensao = nomeDoArquivo.substring(nomeDoArquivo.lastIndexOf('.') + 1, nomeDoArquivo.length())
		}
		return extensao
	}
	
	static boolean ehUmNomeDeArquivoValido( String nomeDoArquivo) {
		if (nomeDoArquivo.lastIndexOf('.') > 0) {
			return true
		}
		return false
	}
	
	static String getMimeTypeByExtension(String extension){
		String mime = null
		if (extension == null) {
			mime = "text/plain"
		} else if (extension.equalsIgnoreCase("png")) {
			mime = "image/png"
		} else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
			mime = "image/jpeg"
		} else if (extension.equalsIgnoreCase("gif")) {
			mime = "image/gif"
		} else if (extension.equalsIgnoreCase("pdf")) {
			mime = "application/pdf"
		} else if (extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx")) {
			mime = "application/vnd.ms-excel"
		} else if (extension.equalsIgnoreCase("csv")) {
			mime = "text/csv"
		} else if (extension.equalsIgnoreCase("txt")) {
			mime = "text/plain"
		} else if (extension.equalsIgnoreCase("doc") || extension.equalsIgnoreCase("docx")) {
			mime = "application/ms-word"
		}
		return mime
	}
	
	static byte[] getBytesFromFile(File arquivo){
		FileInputStream fis = new FileInputStream(arquivo)
		int numberBytes = fis.available()
		byte[] bytes = new byte[numberBytes]
		fis.read(bytes)
		fis.close()
		return bytes
	}
	
	static boolean ehImagem(String extensao) {
		extensao = extensao.toLowerCase()
		return extensao != null && (extensao.equals("png") || extensao.equals("jpg") || extensao.equals("jpeg") || extensao.equals("gif"))
	}
	
	static File getFile(String path) {
		File arquivo = null
		try {
			arquivo = new File(Config.getImagesFolder() + path)
			if (!arquivo.exists()) throw new FileNotFoundException()
		} catch(e) {
			arquivo = new File(this.getClass().getResource("/noimage.gif").getFile())
		}
		return arquivo
	}
	
	static void setFileForDownload(String path, HttpServletResponse response) {
		File arquivo = FileUtil.getFile(path)
		byte[] bytes = FileUtil.getBytesFromFile(arquivo)
		response.addHeader("content-disposition", "attachment filename=" + arquivo.getName())
		response.setContentType(FileUtil.getMimeTypeByExtension(FileUtil.getFileExtension(arquivo.getName())))
		response.setContentLength(bytes.length)
		FileCopyUtils.copy(bytes, response.getOutputStream())
	}
	
}
