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
import java.util.ArrayList;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ykonoclast
 */
public class YAMLHandlerTest
{

    private final YAMLHandler handler = new YAMLHandler("res.".concat((getClass().getModule().getName().replace(".", "-"))));

    @Test
    public void testGetYAMLDocNominal() throws Exception
    {
	//test entre autre que ça gère l'extension auto
	Map<String, Object> map = handler.getYAMLDoc("tab_caracs");

	//test de base
	ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) map.get("arbre_domaines");
	Map<String, Object> map5 = list.get(5);
	ArrayList<String> al = (ArrayList<String>) map5.get("comps");

	ArrayList<String> listEtalon = new ArrayList<>();
	listEtalon.add("culture");
	listEtalon.add("occultisme");
	listEtalon.add("sciences");
	listEtalon.add("stratégie");
	assertEquals(listEtalon, al);
	assertEquals("sciences", al.get(2));

	//test autre fichier, avec des commentaires et en sous-répertoire
	map = handler.getYAMLDoc("sousrep/tab_sys");
	Map<String, ArrayList<Integer>> mapsys = (Map<String, ArrayList<Integer>>) map.get("init");
	ArrayList<Integer> alInt = mapsys.get("mental");
	assertEquals(-1, alInt.get(1).intValue());

	//retour au premier fichier : ça devrait reprendre l'instance, on précise l'extension cette fois-ci
	map = handler.getYAMLDoc("tab_caracs.json");
	ArrayList<String> traits = (ArrayList<String>) map.get("traits");
	assertEquals("volonté", traits.get(3));

    }

    @Test
    public void testGetYAMLDocErreur() throws FileNotFoundException, IOException
    {
	try
	{
	    //test émission exception si fichier non trouvé
	    handler.getYAMLDoc("ragueubuff");
	    fail();
	}
	catch (FileNotFoundException e)
	{
	    assertEquals("file not found: YAML access error: res/org-duckdns-spacedock-commonutils3/YAML/ragueubuff", e.getMessage());
	}
    }

}
