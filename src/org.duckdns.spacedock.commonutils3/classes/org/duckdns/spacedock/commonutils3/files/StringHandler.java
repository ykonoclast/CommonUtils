package org.duckdns.spacedock.commonutils3.files;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Classe chargeant les Strings d'un projet pour une Locale donnée
 *
 * Regles de nommage : les Strings doivent figurer dans deux fichiers :
 * exceptions.properties et generalstrings.properties et être situés dans un
 * répertoire de la forme res/<NOM-MODULE>/strings
 *
 * @author ykonoclast
 */
class StringHandler
{

    /**
     * Textes des exceptions on utilise un resourcebundle plutôt qu'un simple
     * Properties car on a besoin de ses capacités plus avancées (notamment
     * Locale)
     */
    private final ResourceBundle m_exceptionsProperties;

    /**
     * Textes des chaînes générales (pas d'erreurs) on utilise un resourcebundle
     * plutôt qu'un simple Properties car on a besoin de ses capacités plus
     * avancées (notamment Locale)
     */
    private final ResourceBundle m_stringsProperties;

    /**
     * constructeur : charge les deux fichiers de propriétés traités par cette
     * classe pour la locale donnée
     *
     * @param p_instanceFolder le répertoire de ressources considéré
     * @param p_locale
     */
    StringHandler(String p_instanceFolder, Locale p_locale)
    {
	m_stringsProperties = ResourceBundle.getBundle(p_instanceFolder.concat(".strings.generalstrings"), p_locale);
	m_exceptionsProperties = ResourceBundle.getBundle(p_instanceFolder.concat(".strings.exceptions"), p_locale);
    }

    /**
     *
     * @param p_property
     * @return le message d'erreur idoine
     */
    String getErrorMessage(String p_property)
    {
	String result;
	try
	{
	    result = m_exceptionsProperties.getString(p_property);
	}
	catch (MissingResourceException e)
	{
	    result = "";
	}
	return result;
    }

    /**
     *
     * @param p_property
     * @return la chaîne d'usage général (pas de traitement d'erreurs ici)
     * indiquée en paramétre
     */
    String getString(String p_property)
    {
	String result;
	try
	{
	    result = m_stringsProperties.getString(p_property);
	}
	catch (MissingResourceException e)
	{
	    result = "";
	}
	return result;
    }
}
