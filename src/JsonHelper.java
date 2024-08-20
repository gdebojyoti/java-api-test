import java.util.HashMap;
import java.util.Map;

public class JsonHelper {

    private Map<String, Object> data;

    public JsonHelper() {
        data = new HashMap<>();
    }

    public JsonHelper add(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public String build() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        int count = 0;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\": ");
            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }
            if (++count < data.size()) {
                json.append(", ");
            }
        }
        json.append("}");
        return json.toString();
    }
}
