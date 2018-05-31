package com.bert.swagger.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author bo.yang on 17/3/28.
 */
public final class GsonHolder {

    private static final Gson GSON = new Gson();
    private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson SERIALIZE_NULLS_GSON = new GsonBuilder().serializeNulls().create();

    private GsonHolder() {
    }

    public static Gson getGson() {
        return GSON;
    }

    public static Gson getPrettyFormatGson() {
        return PRETTY_GSON;
    }

    public static Gson getSerializeNullsGson() {
        return SERIALIZE_NULLS_GSON;
    }

}
