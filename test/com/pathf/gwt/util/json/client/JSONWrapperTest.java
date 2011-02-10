/*
 * Copyright Pathfinder Development
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.pathf.gwt.util.json.client;

import java.util.Set;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.json.client.JSONString;

/**
 * Created by IntelliJ IDEA.
 * User: dkappe
 * Date: Oct 2, 2008
 * Time: 4:27:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSONWrapperTest extends GWTTestCase {

    public String getModuleName() {
        return "com.pathf.gwt.util.json.jsonwrapper";
    }

    public void testSimpleDrillDown() {
        JSONValue val = JSONParser.parse("{'map' : [['one', { id:1234}],[ 'two', {id:1235}], [null]], 'second':2, third:3, fourth:4}");
        JSONWrapper wrapper = new JSONWrapper(val);

        JSONWrapper result = wrapper.get("map").get(0).get(1).get("id");
        assertFalse(result.isNull());
        assertEquals(result.longValue().intValue(), 1234);
        assertEquals(wrapper.get("map").size(), 3);
        assertFalse(wrapper.get("map").get("other").isNull());
        assertTrue(wrapper.get("map").get(2).get(0).isNull());
	Set<String> keys = wrapper.keySet();
	assertNotNull(keys);
	assertTrue(keys.contains("second"));
	assertEquals(keys.size(),4);
    }
}
