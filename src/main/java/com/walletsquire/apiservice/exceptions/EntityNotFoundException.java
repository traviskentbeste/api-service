package com.walletsquire.apiservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@ResponseStatus(HttpStatus.NOT_FOUND) // make sure to return a 404 instead of a 500
public class EntityNotFoundException extends RuntimeException {

    // zero or more string parameters can be passed
    public EntityNotFoundException(Class clazz, String... searchParamsMap) {
        super(EntityNotFoundException.toMessage(clazz.getSimpleName(), toMap(String.class, String.class, searchParamsMap)));
    }

    // create a custom message
    private static String toMessage(String entity, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) + " was not found for parameters " + searchParams;
    }

    // map entries to a string
    private static <K, V> Map<K, V> toMap(Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1) {
            throw new IllegalArgumentException("Invalid entries");
        }
        return IntStream
                .range(0, entries.length / 2)
                .map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll
                );
    }

}
