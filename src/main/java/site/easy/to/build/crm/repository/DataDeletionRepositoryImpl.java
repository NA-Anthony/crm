package site.easy.to.build.crm.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class DataDeletionRepositoryImpl implements DataDeletionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataDeletionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void disableForeignKeyChecks() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
    }

    @Override
    @Transactional
    public void enableForeignKeyChecks() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");
    }

    @Override
    public List<String> getAllTableNames() {
        String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE() AND table_type = 'BASE TABLE';";
        return jdbcTemplate.queryForList(query, String.class);
    }

    @Override
    @Transactional
    public void deleteTableData(String tableName) {
        String deleteQuery = "DELETE FROM " + tableName;
        jdbcTemplate.execute(deleteQuery);
    }
}