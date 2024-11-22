package mg.itu.prom16.exceptions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationException extends Exception {
    private Map<String, List<String>> errorMap;

    public ValidationException(Map<String, List<String>> errorMap) {
        super("Validation failed");
        this.errorMap = errorMap;
    }

    public Map<String, List<String>> getErrorMap() {
        return errorMap;
    }

    @Override
    public String getMessage() {
        return errorMap.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + String.join(", ", entry.getValue()))
                .collect(Collectors.joining("; "));
    }
}
