package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.easy.to.build.crm.entity.Depense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Integer> {
    @Query(value = "SELECT SUM(d.montant) " +
            "FROM depense d " +
            "LEFT JOIN trigger_ticket tt ON d.id_ticket = tt.ticket_id " +
            "LEFT JOIN trigger_lead tl ON d.id_lead = tl.lead_id " +
            "WHERE tt.customer_id = :customerId OR tl.customer_id = :customerId", nativeQuery = true)
    Double getTotalDepensesByCustomerId(@Param("customerId") Integer customerId);
}