package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.TicketStatusCount;

import java.util.List;

@Repository
public interface TicketStatusCountRepository extends JpaRepository<TicketStatusCount, String> {
    @Query("SELECT t FROM TicketStatusCount t WHERE t.customerId = :customerId")
    List<TicketStatusCount> findByCustomerId(@Param("customerId") int customerId);
}