package site.easy.to.build.crm.service.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.repository.DatabaseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private DatabaseRepository databaseRepository;
    private static final List<String> EXCLUDED_TABLES = Arrays.asList("users", "roles", "oauth_users", "user_profile", "user_roles","customer_login_info");

    @Override
    public List<String> getFilteredTables() {
        // Récupérer toutes les tables
        List<String> allTables = databaseRepository.getAllTables();

        // Filtrer les tables exclues
        return allTables.stream()
                .filter(table -> !EXCLUDED_TABLES.contains(table))
                .collect(Collectors.toList());
    }
}