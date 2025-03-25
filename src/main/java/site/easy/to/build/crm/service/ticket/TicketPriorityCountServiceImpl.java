package site.easy.to.build.crm.service.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.TicketPriorityCount;
import site.easy.to.build.crm.repository.TicketPriorityCountRepository;

import java.util.List;

@Service
public class TicketPriorityCountServiceImpl implements TicketPriorityCountService {

    @Autowired
    private TicketPriorityCountRepository repository;

    @Override
    public List<TicketPriorityCount> getAllPriorityCounts() {
        return repository.findAll();
    }

}