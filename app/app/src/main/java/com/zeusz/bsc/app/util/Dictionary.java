package com.zeusz.bsc.app.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;


/** Wrapper class for {@link JSONObject}. */
public class Dictionary {

    private JSONObject json;

    public Dictionary() {
        this.json = new JSONObject();
    }

    public Dictionary(String json) {
        try {
            this.json = new JSONObject(json);
        }
        catch(JSONException e) {
            this.json = new JSONObject();
        }
    }

    /* Allows chaining */
    public Dictionary put(@NonNull String name, @Nullable Object value) {
        try { json.put(name, value); }
        catch(JSONException e) { /* couldn't add key-value pair */ }

        return this;
    }

    public Dictionary put(Dictionary dictionary) {
        while(dictionary.json.keys().hasNext()) {
            String key = dictionary.json.keys().next();
            put(key, dictionary.get(key));
        }

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

    @NonNull
    @Override
    public String toString() {
        return json.toString();
    }

}
