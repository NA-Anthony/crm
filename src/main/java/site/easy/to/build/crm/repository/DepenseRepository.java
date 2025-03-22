package site.easy.to.build.crm.repository;

import site.easy.to.build.crm.entity.Depense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Integer> {
    // Méthodes personnalisées (si nécessaire)
}