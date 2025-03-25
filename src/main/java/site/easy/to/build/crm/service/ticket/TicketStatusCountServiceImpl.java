package site.easy.to.build.crm.service.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.TicketStatusCount;
import site.easy.to.build.crm.repository.TicketStatusCountRepository;

import java.util.List;

@Service
public class TicketStatusCountServiceImpl implements TicketStatusCountService {

    @Autowired
    private TicketStatusCountRepository repository;

    @Override
    public List<TicketStatusCount> getAllStatusCounts() {
        return repository.findAll();
    }

}