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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ykonoclast
 */
public class PropertiesHandlerTest
{

    private final PropertiesHandler handler = new PropertiesHandler("res.".concat((getClass().getModule().getName().replace(".", "-"))));

    @Test
    public void testGetAppPropertyNominal() throws FileNotFoundException, IOException
    {

	//extension
	String prop = handler.getAppProperty("prop.properties", "truc1");
	assertEquals(prop, "troc1");

	//deuxième valeur atteignable
	prop = handler.getAppProperty("prop.properties", "truc2");
	assertEquals(prop, "troc2");

	//deuxième fichier, sans extension
	prop = handler.getAppProperty("noext", "truc");
	assertEquals(prop, "machin");

	//premier fichier, toujours atteignable
	prop = handler.getAppProperty("prop.properties", "truc2");
	assertEquals(prop, "troc2");//TODO il faudrait trouver un moyen de tester si le fichier n'est bien PAS relu du disque

    }

    @Test
    public void testGetAppPropertyLimiteSousRep() throws FileNotFoundException, IOException
    {

	//extension
	String prop = handler.getAppProperty("sousrep/sousrep.properties", "quoi");
	assertEquals(prop, "sousrep");
    }

    @Test
    public void testGetAppPropertyLimitePropNonExist() throws FileNotFoundException, IOException
    {

	//test chaîne vide si propriété inexistante
	String prop = handler.getAppProperty("prop.properties", "quoi");
	assertEquals("", prop);
    }

    @Test
    public void testGetAppPropertyErreur() throws FileNotFoundException, IOException
    {
	try
	{
	    //test émission exception si fichier non trouvé
	    handler.getAppProperty("ragueubuff", "quoi");
	    fail();
	}
	catch (FileNotFoundException e)
	{
	    assertEquals("file not found: error while accessing properties: res/org-duckdns-spacedock-commonutils3/app/ragueubuff.properties", e.getMessage());
	}
    }
}
