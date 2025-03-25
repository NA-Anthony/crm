package site.easy.to.build.crm.service.ticket;

import site.easy.to.build.crm.entity.TicketStatusCount;

import java.util.List;

public interface TicketStatusCountService {
    List<TicketStatusCount> getAllStatusCounts();
}
