package site.easy.to.build.crm.service.ticket;

import java.util.List;

public interface TicketPriorityCountService<TicketPriorityCount> {
    List<TicketPriorityCount> getAllPriorityCounts();
}