package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.TicketPriorityCount;

import java.util.List;

@Repository
public interface TicketPriorityCountRepository extends JpaRepository<TicketPriorityCount, String> {
    @Query("SELECT t FROM TicketPriorityCount t WHERE t.customerId = :customerId")
    List<TicketPriorityCount> findByCustomerId(@Param("customerId") int customerId);
}