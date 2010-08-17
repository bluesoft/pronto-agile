/*

import java.util.List;
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

package br.com.bluesoft.pronto.controller

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.bluesoft.pronto.core.TipoRetrospectiva
import br.com.bluesoft.pronto.dao.RetrospectivaDao
import br.com.bluesoft.pronto.dao.RetrospectivaItemDao
import br.com.bluesoft.pronto.dao.SprintDao
import br.com.bluesoft.pronto.dao.TipoRetrospectivaDao
import br.com.bluesoft.pronto.dao.TipoRetrospectivaItemDao
import br.com.bluesoft.pronto.model.Anexo;
import br.com.bluesoft.pronto.model.Retrospectiva
import br.com.bluesoft.pronto.model.RetrospectivaItem
import br.com.bluesoft.pronto.service.Config;
import br.com.bluesoft.pronto.util.ControllerUtil
import br.com.bluesoft.pronto.util.FileUtil;
import br.com.bluesoft.pronto.util.StringUtil;
import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/retrospectivas")
class RetrospectivaController {
	
	@Autowired RetrospectivaDao retrospectivaDao
	
	@Autowired TipoRetrospectivaItemDao tipoRetrospectivaItemDao
	
	@Autowired RetrospectivaItemDao retrospectivaItemDao
	
	@Autowired TipoRetrospectivaDao tipoRetrospectivaDao
	
	@Autowired SprintDao sprintDao
	
	@RequestMapping(value="/{retrospectivaKey}", method=GET)
	String ver( Model model, @PathVariable int retrospectivaKey) {
		Retrospectiva retrospectiva = retrospectivaDao.obter(retrospectivaKey)
		return ver(model, retrospectiva)
	}
	
	@RequestMapping("/sprints/{sprintKey}")
	String retrospectiva( Model model, @PathVariable int sprintKey) {
		Retrospectiva retrospectiva = retrospectivaDao.obterRetrospectivaDoSprint(sprintKey)
		if (retrospectiva == null) {
			retrospectiva = new Retrospectiva()
			retrospectiva.setTipoRetrospectiva tipoRetrospectivaDao.obter(TipoRetrospectiva.TRADICIONAL)
			retrospectiva.setSprint sprintDao.obter(sprintKey)
		}
		retrospectivaDao.salvar retrospectiva
		return ver(model, retrospectiva)
	}
	
	@RequestMapping("/{retrospectivaKey}/descricao")
	String descricao( Model model, @PathVariable int retrospectivaKey, String descricao) {
		Retrospectiva retrospectiva = retrospectivaDao.obter(retrospectivaKey)
		retrospectiva.descricao = descricao
		retrospectivaDao.salvar retrospectiva
		return ver(model, retrospectiva)
	}
	
	String ver(Model model, Retrospectiva retrospectiva){
		model.addAttribute("anexos", FileUtil.listarAnexos("retrospectivas/${retrospectiva.retrospectivaKey}/"))
		model.addAttribute "retrospectiva", retrospectiva
		model.addAttribute("sprint", retrospectiva.sprint)
		model.addAttribute "tiposDeRetrospectiva", tipoRetrospectivaDao.listar()
		model.addAttribute "tiposDeItem", tipoRetrospectivaItemDao.listarPorTipoDeRetrospectiva(retrospectiva.getTipoRetrospectiva().getTipoRetrospectivaKey())
		"/retrospectiva/retrospectiva.ver.jsp"
	}
	
	@RequestMapping("/{retrospectivaKey}/alterarTipoDeRetrospectiva")
	String alterarTipoDeRetrospectiva( HttpServletResponse response, @PathVariable int retrospectivaKey,  int tipoRetrospectivaKey) {
		Retrospectiva retrospectiva = retrospectivaDao.obter(retrospectivaKey)
		retrospectiva.setTipoRetrospectiva(tipoRetrospectivaDao.obter(tipoRetrospectivaKey))
		retrospectivaDao.salvar(retrospectiva)
		"redirect:/retrospectivas/sprints/${retrospectiva.sprint.sprintKey}"
	}
	
	@RequestMapping(value = '/{retrospectivaKey}', method=[POST, PUT])
	@ResponseBody String salvarItem( HttpServletResponse response, @PathVariable int retrospectivaKey,  int tipoRetrospectivaItemKey,  String descricao) {
		RetrospectivaItem item = new RetrospectivaItem()
		item.setRetrospectiva(retrospectivaDao.obter(retrospectivaKey))
		item.setTipoRetrospectivaItem(tipoRetrospectivaItemDao.obter(tipoRetrospectivaItemKey))
		item.setDescricao(descricao)
		retrospectivaItemDao.salvar(item)
		item.getRetrospectivaItemKey().toString()
	}
	
	@RequestMapping(value="itens/{retrospectivaItemKey}", method=DELETE)
	@ResponseBody String excluirItem( HttpServletResponse response,  @PathVariable int retrospectivaItemKey) {
		if (retrospectivaItemKey > 0) {
			retrospectivaItemDao.excluir(retrospectivaItemDao.obter(retrospectivaItemKey))
		}
		true.toString()
	}
	
	@RequestMapping(value = '/{retrospectivaKey}/anexos', method = GET)
	void download( HttpServletResponse response,  String file,  @PathVariable int retrospectivaKey)  {
		FileUtil.setFileForDownload("retrospectivas/${retrospectivaKey}/${file}", response)
	}
	
	@RequestMapping("/{retrospectivaKey}/upload")
	String upload( Model model,  HttpServletRequest request, @PathVariable int retrospectivaKey) throws Exception {
		
		FileItemFactory factory = new DiskFileItemFactory()
		ServletFileUpload upload = new ServletFileUpload(factory)
		
		List<FileItem> items = upload.parseRequest(request)
		String folderPath = Config.getImagesFolder() + "retrospectivas/${retrospectivaKey}/"
		File dir = new File(folderPath)
		dir.mkdirs()
		
		List<String> nomesDosArquivos = new ArrayList<String>()

		for ( FileItem fileItem : items) {
			String nomeDoArquivo = StringUtil.retiraAcentuacao(fileItem.getName().toLowerCase().replace(' ', '_')).replaceAll("[^A-Za-z0-9._\\-]", "")
			
			if (FileUtil.ehUmNomeDeArquivoValido(nomeDoArquivo)) {
				fileItem.write(new File(folderPath + nomeDoArquivo))
				nomesDosArquivos.add(nomeDoArquivo)
			} else {
				model.addAttribute("erro", "Não é possível anexar o arquivo '" + nomeDoArquivo + "' pois este não possui uma extensão.")
			}
		}
		
		return "redirect:/retrospectivas/${retrospectivaKey}"
	}
	
	@RequestMapping(value = "/{retrospectivaKey}/anexos", method=DELETE)
	String excluirAnexo(@PathVariable int retrospectivaKey, String file)  {
		File arquivo = new File(Config.getImagesFolder() + "retrospectivas/${retrospectivaKey}/${file}")
		if (arquivo.exists()) {
			arquivo.delete()
		}
		return "redirect:/retrospectivas/${retrospectivaKey}"
	}
}
