package br.com.bluesoft.pronto.service;

import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.model.WikiModel;

public class ProntoWikiModel extends WikiModel {
	
	private static final  String wikiImages = "../tickets/anexos?file=${image}";
	private static final String wikiLinks = "../tickets/${title}";
		
	public ProntoWikiModel() {
		super(wikiImages, wikiLinks);
	}
	
	public void appendInternalImageLink(String link, String raw, ImageFormat img) {
		link = "../tickets/" + link.replaceAll(".*Image:(.*)?\\/.*", "$1") + "/anexos?file=" + link.replaceAll(".*Image:.*?\\/(.*)", "$1");  
		super.appendInternalImageLink(link, raw, img);
	}
	
}
