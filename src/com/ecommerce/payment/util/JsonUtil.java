package com.ecommerce.payment.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal, dependency-free JSON helper used only by the web layer.
 * It intentionally supports just what this sample needs: serializing
 * nested Map/List/String/Number/Boolean structures, and parsing the flat
 * (single-level) JSON object the HTML front-end form posts.
 */
public final class JsonUtil {

    private JsonUtil() {
    }

    // ---- Serialization ---------------------------------------------------

    public static String toJson(Object value) {
        StringBuilder sb = new StringBuilder();
        writeValue(value, sb);
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static void writeValue(Object value, StringBuilder sb) {
        if (value == null) {
            sb.append("null");
        } else if (value instanceof Map) {
            writeObject((Map<String, Object>) value, sb);
        } else if (value instanceof List) {
            writeArray((List<Object>) value, sb);
        } else if (value instanceof String) {
            writeString((String) value, sb);
        } else if (value instanceof Number || value instanceof Boolean) {
            sb.append(value.toString());
        } else {
            writeString(value.toString(), sb);
        }
    }

    private static void writeObject(Map<String, Object> map, StringBuilder sb) {
        sb.append('{');
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                sb.append(',');
            }
            first = false;
            writeString(entry.getKey(), sb);
            sb.append(':');
            writeValue(entry.getValue(), sb);
        }
        sb.append('}');
    }

    private static void writeArray(List<Object> list, StringBuilder sb) {
        sb.append('[');
        boolean first = true;
        for (Object item : list) {
            if (!first) {
                sb.append(',');
            }
            first = false;
            writeValue(item, sb);
        }
        sb.append(']');
    }

    private static void writeString(String s, StringBuilder sb) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
    }

    // ---- Parsing (flat objects only) --------------------------------------

    /**
     * Parses a single-level JSON object such as {"country":"CO","amount":"49.90"}
     * into a Map&lt;String,String&gt;, keeping every value in its raw textual
     * form. This is deliberately simple: the HTML form only ever posts a flat
     * object, so a full recursive parser would be unnecessary complexity here.
     */
    public static Map<String, String> parseFlatObject(String json) {
        Map<String, String> result = new LinkedHashMap<>();
        int i = skipWhitespace(json, 0);
        if (i >= json.length() || json.charAt(i) != '{') {
            throw new IllegalArgumentException("Expected a JSON object");
        }
        i = skipWhitespace(json, i + 1);

        while (i < json.length() && json.charAt(i) != '}') {
            int[] keyEnd = new int[1];
            String key = readString(json, i, keyEnd);
            i = skipWhitespace(json, keyEnd[0]);
            if (json.charAt(i) != ':') {
                throw new IllegalArgumentException("Expected ':' after key '" + key + "'");
            }
            i = skipWhitespace(json, i + 1);

            int[] valueEnd = new int[1];
            String value = readValue(json, i, valueEnd);
            result.put(key, value);
            i = skipWhitespace(json, valueEnd[0]);

            if (i < json.length() && json.charAt(i) == ',') {
                i = skipWhitespace(json, i + 1);
            }
        }
        return result;
    }

    private static String readValue(String json, int start, int[] endOut) {
        char c = json.charAt(start);
        if (c == '"') {
            return readString(json, start, endOut);
        }
        int i = start;
        while (i < json.length() && json.charAt(i) != ',' && json.charAt(i) != '}') {
            i++;
        }
        endOut[0] = i;
        return json.substring(start, i).trim();
    }

    private static String readString(String json, int start, int[] endOut) {
        int i = skipWhitespace(json, start);
        if (json.charAt(i) != '"') {
            throw new IllegalArgumentException("Expected a string at position " + i);
        }
        i++;
        StringBuilder sb = new StringBuilder();
        while (json.charAt(i) != '"') {
            char c = json.charAt(i);
            if (c == '\\') {
                i++;
                char escaped = json.charAt(i);
                switch (escaped) {
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case '"':
                        sb.append('"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    default:
                        sb.append(escaped);
                }
            } else {
                sb.append(c);
            }
            i++;
        }
        i++; // consume closing quote
        endOut[0] = i;
        return sb.toString();
    }

    private static int skipWhitespace(String s, int i) {
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return i;
    }
}
