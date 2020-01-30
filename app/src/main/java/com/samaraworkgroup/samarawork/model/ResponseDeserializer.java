package com.samaraworkgroup.samarawork.model;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ResponseDeserializer implements JsonDeserializer<BonusesList> {
    @Override
    public BonusesList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        BonusesList response = new Gson().fromJson(json, BonusesList.class);

        Map<String, BonusesData> map = new HashMap<>();
        map = (Map<String, BonusesData>) new Gson().fromJson(json, map.getClass());

        response.setDataMap(map);
        return response;
    }
}
