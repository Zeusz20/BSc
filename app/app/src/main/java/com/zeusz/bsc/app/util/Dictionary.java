package com.zeusz.bsc.app.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;


/** Wrapper class for {@link JSONObject}. */
public class Dictionary {

    private final JSONObject json;

    public Dictionary(@Nullable String json) throws JSONException {
        this.json = (json == null) ? new JSONObject() : new JSONObject(json);
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

    @NonNull
    @Override
    public String toString() {
        return json.toString();
    }

}
