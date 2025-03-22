package site.easy.to.build.crm.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseRepository {
    List<String> getAllTables();
}