package site.easy.to.build.crm.service.csv;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class ImportResult {
    private final Map<String, Integer> successCountByTable = new HashMap<>();
    private final List<String> errors = new ArrayList<>();

    public void incrementSuccessCount(String tableName) {
        successCountByTable.merge(tableName, 1, Integer::sum);
    }

    public void addError(String error) {
        errors.add(error);
    }

    // Getters
    public Map<String, Integer> getSuccessCountByTable() {
        return successCountByTable;
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}