package br.com.bluesoft.pronto.service;

import info.bliki.wiki.model.WikiModel;

import org.springframework.web.util.JavaScriptUtils;

public class WikiFormatter {

	private static final String wikiImages = "/${image}";
	private static final String wikiTitles = "/${title}";

	public static String toHtml(final String wiki) {
		final WikiModel wikiModel = new WikiModel(wikiImages, wikiTitles);
		return wikiModel.render(wiki);
	}

	public static String toHtmlJavaScriptEscaped(final String wiki) {
		final WikiModel wikiModel = new WikiModel(wikiImages, wikiTitles);
		return JavaScriptUtils.javaScriptEscape(wikiModel.render(wiki));
	}
}
