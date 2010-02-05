/*
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

package br.com.bluesoft.pronto.service;

import java.io.IOException;
import java.util.Properties;

public class Config {

	private final static Properties properties;

	static {
		properties = new Properties();
		try {
			properties.load(Config.class.getResourceAsStream("/pronto.properties"));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static String getImagesFolder() {
		return properties.get("pronto.imagesFolder").toString();
	}

}
