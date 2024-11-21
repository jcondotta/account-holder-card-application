package com.jcondotta.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jcondotta.exception.SerializationException;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Singleton
public class SerializationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializationService.class);
    private final JsonMapper jsonMapper;

    public SerializationService(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public <T> T deserialize(String json, Class<T> clazz) {
        try {
            return jsonMapper.readValue(json, clazz);
        }
        catch (JsonMappingException e) {
            LOGGER.error("Failed to map JSON to class {}: {}", clazz.getSimpleName(), e.getMessage(), e);
            throw new SerializationException("Invalid JSON structure for class " + clazz.getSimpleName(), e);
        }
        catch (IOException e) {
            LOGGER.error("IO error during deserialization for class {}: {}", clazz.getSimpleName(), e.getMessage(), e);
            throw new SerializationException("IO error during deserialization for class " + clazz.getSimpleName(), e);
        }
    }

    public String serialize(Object object) {
        try {
            return jsonMapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize object of type {}: {}", object.getClass().getSimpleName(), e.getMessage(), e);
            throw new SerializationException("Failed to serialize object of type " + object.getClass().getSimpleName(), e);
        }
        catch (IOException e) {
            LOGGER.error("IO error during serialization for class {}: {}", object.getClass().getSimpleName(), e.getMessage(), e);
            throw new SerializationException("IO error during deserialization for class " + object.getClass().getSimpleName(), e);
        }
    }
}
