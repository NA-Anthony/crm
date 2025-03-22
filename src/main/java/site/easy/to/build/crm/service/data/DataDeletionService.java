package site.easy.to.build.crm.service.data;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.repository.DataDeletionRepository;

import java.util.List;

@Service
public class DataDeletionService {

    private final DataDeletionRepository dataDeletionRepository;
    private static final String[] TABLES_A_EXCLURE = {"users", "roles", "oauth_users", "user_profile", "user_roles"};

    public DataDeletionService(DataDeletionRepository dataDeletionRepository) {
        this.dataDeletionRepository = dataDeletionRepository;
    }

    @Transactional
    public void deleteAllData() {
        // Désactiver les vérifications de clés étrangères
        dataDeletionRepository.disableForeignKeyChecks();

        // Récupérer tous les noms de tables
        List<String> tableNames = dataDeletionRepository.getAllTableNames();

        // Supprimer les données de chaque table (sauf celles exclues)
        for (String tableName : tableNames) {
            if (isTableExcluded(tableName)) {
                continue;
            }
            try {
                dataDeletionRepository.deleteTableData(tableName);
            } catch (Exception e) {
                System.out.println("Erreur sur la table : " + tableName + " -> " + e.getMessage());
            }
        }

        // Réactiver les vérifications de clés étrangères
        dataDeletionRepository.enableForeignKeyChecks();
    }

    private boolean isTableExcluded(String tableName) {
        for (String excludedTable : TABLES_A_EXCLURE) {
            if (excludedTable.equalsIgnoreCase(tableName)) {
                return true;
            }
        }
        return false;
    }
}