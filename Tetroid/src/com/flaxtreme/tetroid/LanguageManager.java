package com.flaxtreme.tetroid;

import java.util.HashMap;
import javax.xml.parsers.*;

import org.w3c.dom.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Language Manager from
 * http://code.google.com/p/freegemas-libgdx/source/browse/
 * trunk/freegemas/src/com/siondream/freegemas/LanguagesManager.java
 * 
 * @author freegemas
 * 
 */
public class LanguageManager {

	private static final String LANGUAGES_FILE = "data/languages.xml";
	private static final String DEFAULT_LANGUAGE = "en_UK";

	// private HashMap<String, HashMap<String, String>> _strings = null;
	private HashMap<String, String> _language = null;
	private String _languageName = null;

	public LanguageManager() {
		// Create language map
		_language = new HashMap<String, String>();

		// Try to load system language
		// If it fails, fallback to default language
		_languageName = java.util.Locale.getDefault().toString();
		//Gdx.app.log("locale", _languageName);
		if (!loadLanguage(_languageName)) {
			loadLanguage(DEFAULT_LANGUAGE);
			_languageName = DEFAULT_LANGUAGE;
		}
	}

	public String getLanguage() {
		return _languageName;
	}

	public String get(String key){
		return getString(key);
	}
	
	public String get(String key, Object... args){
		return getString(key, args);
	}
	
	public String getString(String key) {
		String string;

		if (_language != null) {
			// Look for string in selected language
			string = _language.get(key);

			if (string != null) {
				return string;
			}
		}

		// Key not found, return the key itself
		return key;
	}

	public String getString(String key, Object... args) {
		return String.format(getString(key), args);
	}

	public boolean loadLanguage(String languageName) {
		FileHandle fileHandle = Gdx.files.internal(LANGUAGES_FILE);
		if (!fileHandle.exists()){
			Gdx.app.log(this.getClass().getName(), "Languages file not found: " + LANGUAGES_FILE);
			return false;
		}
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(fileHandle.read());

			Element root = doc.getDocumentElement();

			NodeList languages = root.getElementsByTagName("language");
			int numLanguages = languages.getLength();

			for (int i = 0; i < numLanguages; ++i) {
				Node language = languages.item(i);

				if (language.getAttributes().getNamedItem("name").getNodeValue().equals(languageName)) {
					_language.clear();
					Element languageElement = (Element) language;
					NodeList strings = languageElement.getElementsByTagName("string");
					int numStrings = strings.getLength();

					for (int j = 0; j < numStrings; ++j) {
						NamedNodeMap attributes = strings.item(j).getAttributes();
						String key = attributes.getNamedItem("key").getNodeValue();
						String value = attributes.getNamedItem("value").getNodeValue();
						value = value.replace("<br />", "\n");
						_language.put(key, value);
					}

					return true;
				}
			}
		} catch (Exception e) {
			Gdx.app.log(this.getClass().getName(), "Error loading languages file " + LANGUAGES_FILE);
			return false;
		}

		return false;
	}
}