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

import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ykonoclast
 */
public class StringHandlerTest
{

    private final StringHandler handler = new StringHandler("res.".concat((getClass().getModule().getName().replace(".", "-"))), Locale.getDefault());
    private final StringHandler handlerKO = new StringHandler("res.".concat((getClass().getModule().getName().replace(".", "-"))), Locale.KOREA);
    private final StringHandler handlerEN = new StringHandler("res.".concat((getClass().getModule().getName().replace(".", "-"))), Locale.ENGLISH);

    @Test
    public void testGetStringDefaultLocaleNominal()
    {
	String prop = handler.getString("param_aberr");
	assertEquals("aberrant parameter: ", prop);

	prop = handler.getString("mauv_meth");
	assertEquals("wrong method in this context: ", prop);
    }

    @Test
    public void testGetErrorMessageDefaultLocaleNominal()
    {
	String prop = handler.getErrorMessage("JSON");
	assertEquals(prop, "erreur d'accès JSON:");

	prop = handler.getErrorMessage("properties");
	assertEquals("error while accessing properties: ", prop);
    }

    @Test
    public void testGetStringSpecificLocaleNominal()
    {
	String prop = handlerKO.getString("un");
	assertEquals(prop, "deuxKO");

	prop = handlerKO.getString("trois");
	assertEquals(prop, "quatreKO");

	prop = handlerEN.getString("fich_introuv");
	assertEquals(prop, "file not found: ");
    }

    @Test
    public void testGetErrorMessageSpecificLocaleNominal()
    {
	String prop = handlerKO.getErrorMessage("msg1");
	assertEquals(prop, "erreur1KO");

	prop = handlerKO.getErrorMessage("msg2");
	assertEquals(prop, "erreur2KO");
    }

    @Test
    public void testGetErrorMessageError()
    {
	String prop = handlerKO.getErrorMessage("TAGAZOK");

	assertEquals("", prop);
	prop = handler.getString("blurp");
	assertEquals("", prop);
    }
}
