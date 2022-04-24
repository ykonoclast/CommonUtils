/*
 * Copyright (C) 2022 ykonoclast
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
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

/**
 * classe permettant de récupérer le contenu de fichiers YAML
 *
 * @author ykonoclast
 */
class YAMLHandler
{

    /**
     * le répertoire dans lequel chercher les fichiers YAML
     */
    private final String m_repertoire;

    /**
     * ensemble des fichiers YAML déjà chargés
     */
    private final Map<String, Map<String, Object>> m_yamlDocs = new HashMap<>();

    /**
     * Constructeur
     *
     * @param p_instanceID
     */
    YAMLHandler(String p_instanceID)
    {
	m_repertoire = p_instanceID.replace(".", "/").concat("/YAML/");
    }

    /**
     * Méthode renvoyant le contenu entier du fichier YAML au bout du chemin
     * (relatif au répertoire res/<NOM-MODULE>/app du module) sous la forme
     * d'une map
     *
     * @param p_path
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getYAMLDoc(String p_path) throws FileNotFoundException, IOException
    {
	Map<String, Object> result = null;

	if (!m_yamlDocs.containsKey(p_path))
	{
	    InputStream in = null;
	    String fullPath = m_repertoire.concat(p_path);

	    //on essaye d'abord le chemin donné puis avec diverses extensions au cas où
	    List<String> listPath = new LinkedList<>();
	    listPath.add(fullPath);
	    listPath.add(fullPath.concat(".yaml"));
	    listPath.add(fullPath.concat(".YAML"));
	    listPath.add(fullPath.concat(".json"));
	    listPath.add(fullPath.concat(".JSON"));
	    ListIterator<String> it = listPath.listIterator();

	    while (in == null && it.hasNext())
	    {
		String currentPath = it.next();
		in = Thread.currentThread().getContextClassLoader().getResourceAsStream(currentPath);
	    }

	    if (in != null)
	    {//le fichier a été trouvé
		LoadSettings settings = LoadSettings.builder().setLabel("Custom user configuration").build();
		Load load = new Load(settings);

		result = (Map<String, Object>) load.loadFromInputStream(in);
		in.close();
		m_yamlDocs.putIfAbsent(p_path, result);
	    }
	    else
	    {//le fichier n'a pas été trouvé
		FileResourceHelper.getInstance(getClass().getModule().getName()).fichIntrouvable("YAML", fullPath, Locale.getDefault());
	    }
	}
	else
	{
	    result = m_yamlDocs.get(p_path);
	}
	return result;
    }
}
