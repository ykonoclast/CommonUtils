/*
 * Copyright (C) 2019 ykonoclast
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.duckdns.spacedock.commonutils3.files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Classe chargeant les Properties (hors Strings générales et messages d'erreur)
 * d'un projet
 *
 * Regles de nommage : les fichiers doivent figurer dans le répertoire
 * res/<NOM-MODULE>/app
 *
 * @author ykonoclast
 */
class PropertiesHandler
{

    /**
     * le répertoire dans lequel chercher les fichiers des properties
     */
    private final String m_repertoire;

    /**
     * ensemble des fichiers de properties déjà chargés, on utilise un
     * Properties car on n'a pas besoin des fonctionalités des ResourceBundle
     */
    private final Map<String, Properties> mf_appProperties = new HashMap<>();

    /**
     * Constructeur
     *
     * @param p_instanceID
     */
    PropertiesHandler(String p_instanceID)
    {
	m_repertoire = p_instanceID.replace(".", "/").concat("/app/");
    }

    /**
     * renvoie la property demandée dans le fichier au bout du chemin (relatif
     * au répertoire res/<NOM-MODULE>/app du module) indiqué. Construit l'objet
     * property nécessaire si celui-ci n'existe pas déjà.
     *
     * @param p_path
     * @param p_property
     * @return
     */
    String getAppProperty(String p_path, String p_property) throws FileNotFoundException, IOException
    {
	//InputStream in = PropertiesHandler.class.getClassLoader().getResourceAsStream("strings/exceptions.properties");
	/*on utilise le classloader pour récupérer le fichier de propriétés ailleurs que dans le même package : il utilise le classpath.
	 *On utilise le classloader du thread afin d'être davantage sur qu'il explorera tout le classpath, contrairement au classloader
	 *de la classe (utilisé dans le bout de code commenté ci-dessus). J'ignore si cela marche bien avec les threads android.
	 */

	String result = "";

	if (!mf_appProperties.containsKey(p_path))
	{
	    InputStream in;
	    String fullPath = m_repertoire.concat(p_path);
	    if (!p_path.endsWith(".properties"))
	    {
		fullPath = fullPath.concat(".properties");
	    }
	    in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fullPath);
	    if (in == null)
	    {
		FileResourceHelper.getInstance(getClass().getModule().getName()).fichIntrouvable("properties", fullPath, Locale.getDefault());
	    }
	    else
	    {
		Properties properties = new Properties();
		properties.load(in);
		in.close();

		mf_appProperties.putIfAbsent(p_path, properties);
	    }
	}
	String preRes = mf_appProperties.get(p_path).getProperty(p_property);

	if (preRes != null)
	{
	    result = preRes;
	}
	return result;
    }
}
