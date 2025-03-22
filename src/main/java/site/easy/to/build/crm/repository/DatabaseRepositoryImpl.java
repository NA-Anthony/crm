package site.easy.to.build.crm.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatabaseRepositoryImpl implements DatabaseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getAllTables() {
        String sql = "SELECT table_name " +
                "FROM information_schema.tables " +
                "WHERE table_schema = DATABASE() AND table_type = 'BASE TABLE';";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}