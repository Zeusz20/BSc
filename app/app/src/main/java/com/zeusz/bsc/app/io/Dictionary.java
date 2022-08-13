package com.zeusz.bsc.app.io;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;


public class Dictionary extends JSONObject {

    public Dictionary(String json) throws JSONException {
        super(json);
    }

    @NonNull
    @Override
    public String getString(@NonNull String name) {
        try { return super.getString(name); }
        catch(JSONException e) { return ""; }
    }

    @Override
    public int getInt(@NonNull String name) {
        try { return super.getInt(name); }
        catch(JSONException e) { return -99999; }
    }

}
