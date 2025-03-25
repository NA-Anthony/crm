package site.easy.to.build.crm.service.csv;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenericEntityService {

    @PersistenceContext
    private EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public GenericEntityService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public boolean tableExists(String tableName) {
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName.toUpperCase());
        return count != null && count > 0;
    }

    @Transactional
    public void save(String tableName, Map<String, String> rowData) {
        if (rowData == null || rowData.isEmpty()) {
            throw new IllegalArgumentException("Les données de la ligne ne peuvent pas être vides");
        }

        Set<String> columns = getTableColumns(tableName);
        Map<String, String> filteredRow = filterColumns(rowData, columns);

        if (filteredRow.isEmpty()) {
            throw new IllegalArgumentException("Aucune colonne valide trouvée pour la table " + tableName);
        }

        insertRow(tableName, filteredRow);
    }

    private Set<String> getTableColumns(String tableName) {
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sql, String.class, tableName.toUpperCase()));
    }

    private Map<String, String> filterColumns(Map<String, String> rowData, Set<String> validColumns) {
        return rowData.entrySet().stream()
                .filter(entry -> validColumns.contains(entry.getKey().toLowerCase()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing
                ));
    }

    private void insertRow(String tableName, Map<String, String> rowData) {
        String columns = String.join(", ", rowData.keySet());
        String placeholders = rowData.keySet().stream()
                .map(key -> ":" + key)
                .collect(Collectors.joining(", "));

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);
        namedJdbcTemplate.update(sql, new MapSqlParameterSource(rowData));
    }
}