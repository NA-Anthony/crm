package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.easy.to.build.crm.entity.Depense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Integer> {

    // Récupérer une dépense par ID de ticket
    @Query("SELECT d FROM Depense d WHERE d.ticket.id = :ticketId")
    Depense findByTicketId(@Param("ticketId") Integer ticketId);

    // Récupérer une dépense par ID de ticket
    @Query("SELECT d FROM Depense d WHERE d.lead.id = :leadId")
    Depense findByLeadId(@Param("leadId") Integer leadId);

    // Requête personnalisée pour obtenir le total des dépenses par ID de client
    @Query(value = "SELECT SUM(d.montant) " +
            "FROM depense d " +
            "LEFT JOIN trigger_ticket tt ON d.id_ticket = tt.ticket_id " +
            "LEFT JOIN trigger_lead tl ON d.id_lead = tl.lead_id " +
            "WHERE tt.customer_id = :customerId OR tl.customer_id = :customerId", nativeQuery = true)
    Double getTotalDepensesByCustomerId(@Param("customerId") Integer customerId);
}