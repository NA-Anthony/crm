package site.easy.to.build.crm.config;

import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.Map;

@Configuration
public class TableConfig {
    private final Map<String, List<String>> tableMappings = Map.of(
            "customer", List.of(
                    "customer_id", "name", "phone", "address", "city",
                    "state", "country", "user_id", "description", "position",
                    "twitter", "facebook", "youtube", "created_at", "email", "profile_id"
            ),
            "customer_login_info", List.of(
                    "id", "password", "username", "token", "password_set"
            )
    );

    private final Map<String, String> requiredColumns = Map.of(
            "customer", "name,email",
            "customer_login_info", "username,password"
    );

    public Map<String, List<String>> getTableMappings() {
        return tableMappings;
    }

    public Map<String, String> getRequiredColumns() {
        return requiredColumns;
    }

    public boolean isValidColumn(String tableName, String columnName) {
        return tableMappings.getOrDefault(tableName, List.of()).contains(columnName);
    }
}