package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.easy.to.build.crm.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    @Query(value = "SELECT SUM(montant) FROM budget WHERE id_customer = :customerId", nativeQuery = true)
    Double getTotalBudgetByCustomerId(@Param("customerId") Integer customerId);
}