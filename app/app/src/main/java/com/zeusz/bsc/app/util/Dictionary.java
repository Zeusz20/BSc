package com.zeusz.bsc.app.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Dictionary {

    private JSONObject json;
    private Map<String, Object> values;

    public Dictionary(@Nullable String json) throws JSONException {
        this.json = (json == null) ? new JSONObject() : new JSONObject(json);
        this.values = new HashMap<>();
    }

    /* Allows chaining */
    public Dictionary put(@NonNull String name, @Nullable Object value) {
        try { json.put(name, value); }
        catch(JSONException e) { /* couldn't add key-value pair */ }

        return this;
    }

    protected java.lang.Object get(@NonNull String name) {
        try { return json.get(name); }
        catch(JSONException e) { return null; }
    }

    public String getString(@NonNull String name) {
        return (String) get(name);
    }

    public Integer getInt(@NonNull String name) {
        return (Integer) get(name);
    }

    public Boolean getBoolean(@NonNull String name) {
        return (Boolean) get(name);
    }

    /**
     * Only call this right before sending the dictionary object to
     * the server because this will clear the "values" map.
     * */
    @NonNull
    @Override
    public String toString() {
        values.keySet().forEach(it -> put(it, values.get(it)));
        values.clear();

        String result = json.toString();
        json = new JSONObject();

        return result;
    }

}
