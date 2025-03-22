package site.easy.to.build.crm.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DataDeletionRepository {

    @Transactional
    void disableForeignKeyChecks();

    @Transactional
    void enableForeignKeyChecks();

    List<String> getAllTableNames();

    @Transactional
    void deleteTableData(String tableName);
}