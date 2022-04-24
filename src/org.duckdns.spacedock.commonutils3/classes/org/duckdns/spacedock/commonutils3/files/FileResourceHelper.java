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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Classe principale d'accès aux utilitaires de récupération de fichiers
 *
 * @author ykonoclast
 */
public class FileResourceHelper
{

    /**
     * instances statiques uniques par module
     */
    private final static Map<String, FileResourceHelper> m_instances = new HashMap<>();

    /**
     * nom du répertoire de ressources de cette instance (aussi utilisé comme
     * identifiant unique de l'instance elle-même) de la forme res.<NOM-MODULE>
     * le nom du module étant tireté
     */
    private final String m_instanceFolder;

    /**
     * ensemble des StringHandlers de cette instance de module par locale
     */
    private final Map<Locale, StringHandler> m_stringsHandlers = new HashMap<>();

    /**
     * PropertiesHandler de cette instance de module
     */
    private PropertiesHandler m_PropertiesHandler;

    /**
     * YAMLHandler de cette instance de module
     */
    private YAMLHandler m_yamlHandler;

    /**
     * pseudo constructeur : renvoie l'instance correspondant au module indiqué
     * et la construit si elle n'existe pas
     *
     * @param p_module
     * @return
     */
    public static FileResourceHelper getInstance(String p_module)
    {
	String undottedModule = p_module.replace(".", "-");

	if (!m_instances.containsKey(undottedModule))
	{
	    m_instances.put(undottedModule, new FileResourceHelper(undottedModule));
	}

	return m_instances.get(undottedModule);
    }

    /**
     * véritable contructeur, appelé par getInstance() si l'instance n'existe
     * pas. Ne construit rien tout de suite : on le fera à la volée sur demande
     * uniquement
     *
     */
    private FileResourceHelper(String p_instanceID)
    {
	m_instanceFolder = "res.".concat((p_instanceID));
    }

    /**
     * Renvoie la string demandée du fichier nommé "generalstrings.properties"
     * situé dans le répertoire res/<NOM.DU.MODULE>/strings du module appelant.
     * Prend en compte la locale
     *
     * @param p_property
     * @param p_locale
     * @return
     */
    public String getString(String p_property, Locale p_locale)
    {
	retrieveStringHandler(p_locale);

	return m_stringsHandlers.get(p_locale).getString(p_property);
    }

    /**
     * envoyer une exception IllegalArgumentException avec un message d'erreur
     * standardisé issu du fichier nommé "exceptions.properties" situé dans le
     * répertoire res/<NOM.DU.MODULE>/strings du module appelant. Prend en
     * compte la locale
     *
     * @param p_propExcep
     * @param p_locale
     */
    public void paramAberrant(String p_propExcep, Locale p_locale)
    {
	paramAberrant(p_propExcep, "", p_locale);
    }

    /**
     * envoyer une exception IllegalArgumentException avec un message d'erreur
     * standardisé issu du fichier nommé "exceptions.properties" situé dans le
     * répertoire res/<NOM.DU.MODULE>/strings du module appelant ainsi qu'un
     * complément personnalisé. Prend en compte la locale
     *
     * @param p_propExcep
     * @param p_complementTexte
     * @param p_locale
     */
    public void paramAberrant(String p_propExcep, String p_complementTexte, Locale p_locale)
    {
	String msg = baseExcep("param_aberr", p_propExcep, p_locale).concat(p_complementTexte);
	throw new IllegalArgumentException(msg);
    }

    /**
     * envoyer une exception IllegalStateException avec un message d'erreur
     * standardisé issu du fichier nommé "exceptions.properties" situé dans le
     * répertoire res/<NOM.DU.MODULE>/strings du module appelant. Prend en
     * compte la locale
     *
     * @param p_propExcep
     * @param p_locale
     */
    public void mauvaiseMethode(String p_propExcep, Locale p_locale)
    {
	mauvaiseMethode(p_propExcep, "", p_locale);
    }

    /**
     * envoyer une exception IllegalStateException avec un message d'erreur
     * standardisé issu du fichier nommé "exceptions.properties" situé dans le
     * répertoire res/<NOM.DU.MODULE>/strings du module appelant ainsi qu'un
     * complément personnalisé. Prend en compte la locale
     *
     * @param p_propExcep
     * @param p_complementTexte
     * @param p_locale
     */
    public void mauvaiseMethode(String p_propExcep, String p_complementTexte, Locale p_locale)
    {
	String msg = baseExcep("mauv_meth", p_propExcep, p_locale).concat(p_complementTexte);
	throw new IllegalStateException(msg);
    }

    /**
     * envoyer une exception FileNotFoundException avec un message d'erreur
     * standardisé issu du fichier nommé "exceptions.properties" situé dans le
     * répertoire res/<NOM.DU.MODULE>/strings du module appelant. Prend en
     * compte la locale
     *
     * @param p_propExcep
     * @param p_locale
     * @throws java.io.FileNotFoundException
     */
    public void fichIntrouvable(String p_propExcep, Locale p_locale) throws FileNotFoundException
    {
	fichIntrouvable(p_propExcep, "", p_locale);
    }

    /**
     * envoyer une exception FileNotException avec un message d'erreur
     * standardisé issu du fichier nommé "exceptions.properties" situé dans le
     * répertoire res/<NOM.DU.MODULE>/strings du module appelant ainsi qu'un
     * complément personnalisé Prend en compte la locale
     *
     * @param p_propExcep
     * @param p_complementTexte
     * @param p_locale
     * @throws java.io.FileNotFoundException
     */
    public void fichIntrouvable(String p_propExcep, String p_complementTexte, Locale p_locale) throws FileNotFoundException
    {
	String msg = baseExcep("fich_introuv", p_propExcep, p_locale).concat(p_complementTexte);
	throw new FileNotFoundException(msg);
    }

    /**
     * méthode utilitaire construisant la base du message d'erreur ensuite
     * utilisé par toutes les méthodes relatives aux exceptions
     *
     * @param p_typeExcep en fonction des strings définies pour commonutils
     * @param p_propExcep
     * @param p_locale
     * @return
     */
    private String baseExcep(String p_typeExcep, String p_propExcep, Locale p_locale)
    {
	retrieveStringHandler(p_locale);

	String message = getInstance(getClass().getModule().getName()).getString(p_typeExcep, p_locale);//le message d'érreur standard : issu de commonutils
	message = message.concat(m_stringsHandlers.get(p_locale).getErrorMessage(p_propExcep));//le message d'erreur fourni par le module appelant
	return (message);
    }

    /**
     * méthode fournissant un StringHandler correspondant à la locale à la
     * demande : il est créé si il n'existe pas déjà
     *
     * @param p_locale
     */
    private void retrieveStringHandler(Locale p_locale)
    {
	if (!m_stringsHandlers.containsKey(p_locale))
	{
	    StringHandler handler = new StringHandler(m_instanceFolder, p_locale);
	    m_stringsHandlers.put(p_locale, handler);
	}
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
    public Map<String, Object> getYamlDoc(String p_path) throws FileNotFoundException, IOException
    {
	if (m_yamlHandler == null)
	{
	    m_yamlHandler = new YAMLHandler(m_instanceFolder);
	}
	return m_yamlHandler.getYAMLDoc(p_path);
    }

    /**
     * renvoie une propriété définie par son nom et située dans le fichier au
     * bout du chemin (relatif au répertoire res/<NOM-MODULE>/app du module)
     * indiqué
     *
     * @param p_baseFilePath le nom du fichier et PAS son chemin complet
     * @param p_property
     * @return
     * @throws java.io.FileNotFoundException
     */
    public String getAppProperty(String p_baseFilePath, String p_property) throws FileNotFoundException, IOException
    {
	if (m_PropertiesHandler == null)
	{
	    m_PropertiesHandler = new PropertiesHandler(m_instanceFolder);
	}

	return m_PropertiesHandler.getAppProperty(p_baseFilePath, p_property);
    }
}
