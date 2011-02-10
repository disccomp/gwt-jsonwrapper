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

import com.google.gwt.json.client.*;

import java.util.Set;

/**
 * Wrapper for GWT's <code>JSONValue</code> class.
 *
 * This class allows for the easy chaining of operators without having to constantly check for null
 * return values. So, instead of typical <code>JSONValue</code> code
 * <pre>
 *JSONValue root = JSONParser.parse(json);
 *JSONObject obj = root.isObject();
 *if (obj != null) {
 *    JSONValue map = obj.get("map");
 *    if (map != null) {
 *        JSONArray arr = map.isArray();
 *        if (arr != null) {
 *            if (arr.size() > 1) {
 *                JSONValue strval = arr.get(1);
 *                if (strval != null) {
 *                    JSONString str = strval.isString();
 *                    if (str != null) {
 *                        String s = str.stringValue();
 *                        // do something with the string
 *                    }
 *                }
 *            }
 *        }
 *    }
 *}
 *</pre>
 *
 * we can write something like <code>String result = obj.get("map").get(1).stringValue()</code>
 * without having to worry about <code>NullPointerException</code>'s cropping up all over the place.
 */
public class JSONWrapper {
    private JSONValue value;
	/**
	 * private String[] keys  Stores the keys of the <code>JSONWrapper</code> Object on which the {@link #iterator()} method has been called.
	 */
	private String[]  keys;
	/**
	 * private String[] index  Stores the current index position of the <code>JSONWrapper</code> Object on which the {@link #iterator()} method has been called.
	 */
	private Integer index;

    /**
     * Creates a <code>JSONWrapper</code> object from the supplied <code>JSONValue</code> argument.
     * 
     * @param value A <code>JSONValue</code> value.
     */
    public JSONWrapper(JSONValue value) {
        this.value = value;
    }
    /**
     * Return the wrapped <code>JSONValue</code> value.
     *
     * @return The wrapped <code>JSONValue</code>.
     */
    public JSONValue getValue() {
        return value;
    }

	/**
	 * Returns an iterable <code>JSONWrapper</code> wrapped <code>JSONValue</code> using {@link #hasNext()}  and {@link #next()}, or <code>INVALID</code> if the wrapped value is not an array or if the index is out of bounds.
	 * @return An iterable <code>JSONWrapper</code> wrapped <code>JSONValue</code>, <code>JSONWrapper</code> wrapped <code>JSONValue</code>, <code>INVALID</code> if the
	 * wrapped value is not an array or if the index is out of bounds.
	 * @see #hasNext()
	 * @see #next()
	 * @see #getKeys()
	 * @see #INVALID
	 */
	public JSONWrapper iterator(){

		_iterator();
		if(index == -1){
			return this;
		}else{
			return null;
		}
	}

	private void _iterator(){
		JSONArray arr = value.isArray();
		if( arr != null){
			keys = new String[arr.size()];
			index = -1;			
		}
		JSONObject obj = value.isObject();        
		if( obj != null){
			keys = obj.keySet().toArray(new String[0]);
			index = -1;			
		}
	}

	/**
	 * @return Whether there are any more keys to iterate over
	 */
	public boolean hasNext(){    	
		try {
			boolean returnVal = index < keys.length - 1;
			return returnVal;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * @return A <code>JSONWrapper</code> wrapped <code>JSONValue</code>, <code>INVALID</code> if the
	 * wrapped value is not an array or if the index is out of bounds.
	 */
	public JSONWrapper next(){
		if(!hasNext()){return null;}

		if(value.isArray()!=null){
			return get(++index);
		}
		if(value.isObject()!=null){
			return get(keys[++index]);
		}

		return INVALID;
	}

	/**
	 * @return The current {@link #index} (Not the current {@link #keys key}) of the <code>JSONWrapper</code> Object on which the {@link #iterator()} method has been called. 
	 * @see #getKeys()
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @return The Current key
	 */
	public String getKey() {
		return keys[index];
	}

	/**
	 * @return String[] of an Object's keys
	 */
	public String[] getKeys() {
		return keys;
	}
	/**
	 * @return String[] of a JSONValue
	 */
	public String[] getStringArray(){

		_iterator();//Make self iterable

		String[] returnVal = new String[size()];
		while(hasNext()){			 
			returnVal[getIndex()+1] = next().stringValue();
		}

		return returnVal;
	}
	public JSONWrapper last(){
		if (value == null) {
			return INVALID;
		}

		if(value.isArray() != null){
			return get(value.isArray().size() - 1);
		}

		if(value.isObject() != null){
			Set<String> keys = value.isObject().keySet();
			String lastkey = (String) keys.toArray() [keys.size() - 1];
			return get(lastkey);
		}else{
			return INVALID;
		}
	}

	public JSONWrapper first(){
		if (value == null) {
			return INVALID;
		}

		if(value.isArray() != null){
			return get(0);
		}

		if(value.isObject() != null){
			Set<String> keys = value.isObject().keySet();
			String firstkey = (String) keys.toArray() [0];
			return get(firstkey);
		}else{
			return INVALID;
		}
	}

    /**
     * Constant value that represents an invalid <code>JSONValue</code> value.
     * We reuse this constant to avoid chewing
     * up memory. It will return a <code>null</code> as its wrapped value.
     */
    public final static JSONWrapper INVALID = new JSONWrapper(null);

    /**
     * Get the index-th item in the array.
     *
     * @param index non-negative integer
     * @return A <code>JSONWrapper</code> wrapped <code>JSONValue</code>, <code>INVALID</code> if the
     * wrapped value is not an array or if the index is out of bounds.
     * @see #INVALID
     */
    public JSONWrapper get(int index) {
        if (index < 0) {
            return INVALID;
        }
        if (value == null) {
            return INVALID;
        }

        JSONArray arr = value.isArray();
        if (arr == null) {
            return INVALID;
        }
        if (index >= arr.size()) {
            return INVALID;
        }
        JSONValue retval = arr.get(index);
        if (retval == null) {
            return INVALID;
        }

        return new JSONWrapper(retval);
    }

    /**
     * Get the size of the array.
     *
     * @return A non-negative integer representing the size of the array. Zero if the array has no elements
     * or is not a <code>JSONArray</code>.
     */
    public int size() {
        if (value == null) {
            return 0;
        }

        JSONArray arr = value.isArray();
        if (arr == null) {
            return 0;
        }

        return arr.size();
    }

    /**
     * Get the item that corresponds to the <code>key</code> in the map.
     *
     * @param key a string
     * @return A <code>JSONWrapper</code> wrapped <code>JSONValue</code>, <code>INVALID</code> if the
     * wrapped value is not an <code>JSONObject</code> or if the <code>key</code> doesn't have
     * a corresponding value.
     * @see #INVALID
     */
    public JSONWrapper get(String key) {
        if (value == null) {
            return INVALID;
        }

        JSONObject obj = value.isObject();
        if (obj == null) {
            return INVALID;
        }
        JSONValue retval = obj.get(key);
        if (retval == null) {
            return INVALID;
        }

        return new JSONWrapper(retval);
    }

    /**
     * Return the <code>Set</code> of strings that make up the key set.
     *
     * @return The set of string keys of the <code>JSONObject</code> map.
     * @see JSONObject
     */
    public Set<String> keySet() {
        if (value == null) {
            return null;
        }

        JSONObject obj = value.isObject();
        if (obj == null) {
            return null;
        }
        return obj.keySet();
    }

    /**
     * Uses the <code>equals</code> method of the underlying <code>JSONValue</code> object.
     *
     * If the wrapped value is <code>null</code>, it is equal to other wrapped <code>null</code>'s.
     *
     * @return <code>true</code> if equal, <code>false</code> if not.
     */
    public boolean equals(Object o) {
        if ((o == null) || (!(o instanceof JSONWrapper))) {
            return false;
        }
        JSONWrapper other = (JSONWrapper)o;
        if ((value == null) && (other.value != null)) {
            return false;
        }
        if ((value != null) && (other.value == null)) {
            return false;
        }
        if ((value == null) && (other.value == null)) {
            return true;
        }
        return value.equals(other.value);
    }
    
    /**
     * Uses the <code>hashcode</code> method of the underlying <code>JSONValue</code> object, or of the
     * <code>Object</code> if that value is <code>null</code>.
     *     
     * @return an integer hash code
     */
    public int hashCode() {
        if (value == null) {
            return super.hashCode();
        } else {
            return value.hashCode();
        }
    }

    /**
     * Is the wrapped <code>JSONValue</code> a <code>JSONNull</code>?
     *
     * @return <code>true</code> if the underlying value is a <code>JSONNull</code>, <code>false</code>
     * otherwise.
     */
    public boolean isNull() {
        if (value == null) {
            return false;
        }
        return (value.isNull() != null);
    }


        /**
     * Return the boolean value of the wrapped <code>JSONValue</code>.
     *
     * @return A <code>Boolean</code> object if the wrapped value is a <code>JSONBoolean</code>,
     * <code>null</code> otherwise.
     */
    public Boolean booleanValue() {
        if (value == null) {
            return null;
        }
        JSONBoolean b = value.isBoolean();
        if (b == null) {
            return null;
        }
        return b.booleanValue(); // autoboxing
    }

    /**
     * Return the string value of the wrapped <code>JSONValue</code>.
     *
     * @return A <code>String</code> object if the wrapped value is a  <code>JSONString</code>,
     * <code>null</code> otherwise.
     */
    public String stringValue() {
        if (value == null) {
            return null;
        }
        JSONString str = value.isString();
        if (str == null) {
            return null;
        }
        return str.stringValue();
    }

    /**
     * Return the double value of the wrapped <code>JSONValue</code>.
     *
     * @return A <code>Double</code> object if the wrapped value is a  <code>JSONNumber</code>,
     * <code>null</code> otherwise.
     */
    public Double numberValue() {
        if (value == null) {
            return null;
        }
        JSONNumber num = value.isNumber();
        if (num == null) {
            return null;
        }
        return num.doubleValue(); // autoboxing
    }

    /**
     * Return the long integer value of the wrapped <code>JSONValue</code>.
     *
     * @return A <code>Long</code> object if the wrapped value is a  <code>JSONNumber</code>, <code>null</code>
     * otherwise.
     */
    public Long longValue() {
        if (value == null) {
            return null;
        }
        JSONNumber num = value.isNumber();
        if (num == null) {
            return null;
        }
        double val = num.doubleValue();
        long retval = (long)val;
	return retval; // autoboxing
    }

    /**
     * Return the integer value of the wrapped <code>JSONValue</code>.
     *
     * @return An <code>Integer</code> object if the wrapped value is a  <code>JSONNumber</code>, <code>null</code>
     * otherwise.
     */
    public Integer intValue() {
        if (value == null) {
            return null;
        }
        JSONNumber num = value.isNumber();
        if (num == null) {
            return null;
        }
        double val = num.doubleValue();
        int retval = (int)val;
        return retval; // autoboxing
    }

    /**
     * Is the wrapped <code>JSONValue</code> a <code>JSONArray</code>?
     *
     * @return <code>true</code> if the underlying value is a <code>JSONArray</code>, <code>false</code>
     * otherwise.
     */
    public boolean isArray() {
        if (value == null) {
            return false;
        }
        return (value.isArray() != null);
    }

    /**
     * Is the wrapped <code>JSONValue</code> a <code>JSONObject</code>?
     *
     * @return <code>true</code> if the underlying value is a <code>JSONObject</code>, <code>false</code>
     * otherwise.
     */
    public boolean isObject() {
        if (value == null) {
            return false;
        }
        return (value.isObject() != null);
    }

    /**
     * Is the wrapped <code>JSONValue</code> a <code>JSONString</code>?
     *
     * @return <code>true</code> if the underlying value is a <code>JSONString</code>, <code>false</code>
     * otherwise.
     */
    public boolean isString() {
        if (value == null) {
            return false;
        }
        return (value.isString() != null);
    }

    /**
     * Is the wrapped <code>JSONValue</code> a <code>JSONNumber</code>?
     *
     * @return <code>true</code> if the underlying value is a <code>JSONNumber</code>, <code>false</code>
     * otherwise.
     */
    public boolean isNumber() {
        if (value == null) {
            return false;
        }
        return (value.isNumber() != null);
    }

    /**
     * Is the wrapped <code>JSONValue</code> a valid <code>JSONValue</code> or <code>null</code>?
     *
     * @return <code>true</code> if the underlying value is a <code>JSONValue</code>, <code>false</code>
     * otherwise.
     */
    public boolean isValid() {
        return (value != null);
    }
}
