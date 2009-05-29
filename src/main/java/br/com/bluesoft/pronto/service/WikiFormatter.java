package br.com.bluesoft.pronto.service;

import info.bliki.wiki.model.WikiModel;

public class WikiFormatter {

	private static final String wikiImages = "http://www.mywiki.com/wiki/${image}";
	private static final String wikiTitles = "http://www.mywiki.com/wiki/${title}";

	public static String toHtml(String wiki) {
		WikiModel wikiModel = new WikiModel(wikiImages, wikiTitles);
		return wikiModel.render(wiki);
	}
}
