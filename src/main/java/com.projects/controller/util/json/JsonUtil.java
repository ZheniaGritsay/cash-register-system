package com.projects.controller.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String createJson(Object object) {
        String json = "";
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static String readFromRequest(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static JsonNode getJsonTree(String json) {
        JsonNode jsonTree = null;
        try {
            jsonTree = mapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonTree;
    }

    public static <T> T parseJson(String json, Class<T> clazz) {
        T obj = null;
        try {
            obj = mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JsonNode joinJson(JsonNode node, String nodeName, JsonNode otherNode, String otherNodeName) {
        JsonNode jsonNode = mapper.createObjectNode();
        ((ObjectNode) jsonNode).set(nodeName, node);
        ((ObjectNode) jsonNode).set(otherNodeName, otherNode);

        return jsonNode;
    }

    public static String joinJson(String json, String nodeName, String otherJson, String otherNodeName) {
        String joinedJson = "";
        try {
            JsonNode jsonNode = mapper.readTree(json);
            JsonNode otherJsonNode = mapper.readTree(otherJson);
            JsonNode joinedNode = joinJson(jsonNode, nodeName, otherJsonNode, otherNodeName);
            joinedJson = mapper.writeValueAsString(joinedNode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return joinedJson;
    }

    public static String addToJson(String json, String jsonToAdd, String jsonToAddName) {
        String combinedJson = "";
        try {
            JsonNode jsonNode = mapper.readTree(json);
            JsonNode jsonToAddNode = mapper.readTree(jsonToAdd);
            ((ObjectNode) jsonNode).set(jsonToAddName, jsonToAddNode);
            combinedJson = mapper.writeValueAsString(jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return combinedJson;
    }

}
