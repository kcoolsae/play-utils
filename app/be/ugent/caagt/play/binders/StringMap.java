/*
 * StringMap.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.play.binders;

import play.mvc.QueryStringBindable;

import java.io.Serial;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a map of strings to strings that can be used in a query string.
 *
 * Note: currently cannot be used in Javascript routes.
 */
public class StringMap extends HashMap<String,String> implements QueryStringBindable<StringMap> {

    /**
     * Creates an empty string map. Needed for query string binding to work.
     */
    public StringMap () {

    }

    /**
     * Initializes a string map by copying certain keys from another map (obtained from a dynamic form, say)
     */
    public StringMap(Map<String,String> source, String... keys) {
        for (String key : keys) {
            String value = source.get(key);
            if (value != null && !value.isBlank()) {
                put (key, value);
            }
        }
    }

    /**
     * Same as {@link #StringMap(Map, String...)} but uses the values of the given enum class as strings
     */
    public <E extends Enum<E>> StringMap(Map<String,String> source, Class<E> enumClass) {
        for (E element : enumClass.getEnumConstants()) {
            String key = element.name();
            String value = source.get(key);
            if (value != null && !value.isBlank()) {
                put (key, value);
            }
        }
    }

    @Override
    public Optional<StringMap> bind(String key, Map<String, String[]> data) {
        String prefix = key + ".";
        for (Entry<String, String[]> entry : data.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                String[] values = entry.getValue();
                if (values.length > 1) {
                    return Optional.empty(); //
                } else if (values.length == 1 && !values[0].trim().isEmpty()) {
                    put(entry.getKey().substring(prefix.length()), values[0]);
                }
            }
        }
        return Optional.of(this);
    }

    @Override
    public String unbind(String key) {
        if (isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Entry<String, String> entry : entrySet()) {
            String value = entry.getValue();
            if (value != null && !value.trim().isEmpty()) {
                builder.append("&")
                        .append(key).append(".").append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            }
        }
        return builder.substring(1);
    }

    @Override
    public String javascriptUnbind() {
        return null;         // TODO?

    }
}
