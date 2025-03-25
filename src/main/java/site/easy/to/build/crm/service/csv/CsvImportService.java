package site.easy.to.build.crm.service.csv;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.config.TableConfig;
import site.easy.to.build.crm.exception.CsvImportException;
import site.easy.to.build.crm.util.CsvUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CsvImportService {

    private final GenericEntityService entityService;
    private final TableConfig tableConfig;

    public CsvImportService(GenericEntityService entityService, TableConfig tableConfig) {
        this.entityService = entityService;
        this.tableConfig = tableConfig;
    }

    public ImportResult importCsv(MultipartFile file) throws CsvImportException {
        try {
            if (file.isEmpty()) {
                throw new CsvImportException("Le fichier CSV est vide");
            }

            List<Map<String, String>> csvData = CsvUtils.readCsv(file.getInputStream());
            return processMultiTableImport(csvData);

        } catch (IOException e) {
            throw new CsvImportException("Erreur de lecture du fichier CSV", e);
        }
    }

    private ImportResult processMultiTableImport(List<Map<String, String>> csvData) {
        ImportResult result = new ImportResult();
        Set<String> availableTables = tableConfig.getTableMappings().keySet();

        for (Map<String, String> row : csvData) {
            String tableName = row.get("table_name");

            if (!availableTables.contains(tableName)) {
                result.addError("Table non supportée: " + tableName);
                continue;
            }

            try {
                validateRow(tableName, row);
                entityService.save(tableName, filterValidColumns(tableName, row));
                result.incrementSuccessCount(tableName);
            } catch (Exception e) {
                result.addError("Erreur ligne " + (csvData.indexOf(row) + 1) + ": " + e.getMessage());
            }
        }

        return result;
    }

    private void validateRow(String tableName, Map<String, String> row) throws CsvImportException {
        // Validation des champs requis
        String requiredColumns = tableConfig.getRequiredColumns().get(tableName);
        if (requiredColumns != null) {
            for (String col : requiredColumns.split(",")) {
                if (row.get(col.trim()).isBlank()) {
                    throw new CsvImportException("Champ requis manquant: " + col);
                }
            }
        }
    }

    private Map<String, String> filterValidColumns(String tableName, Map<String, String> row) {
        return row.entrySet().stream()
                .filter(entry -> tableConfig.isValidColumn(tableName, entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}