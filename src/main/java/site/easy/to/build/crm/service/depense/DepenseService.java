package site.easy.to.build.crm.service.depense;

import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Ticket;

import java.util.List;

public interface DepenseService {
    List<Depense> getAllDepenses();
    Depense getDepenseById(Integer id);
    Depense createDepense(Depense depense);
    Depense updateDepense(Integer id, Depense depense);
    void deleteDepense(Integer id);
    Double getTotalDepensesByCustomerId(Integer customerId);
    Depense findByTicketId(Integer ticketId);
    Depense findByLeadId(Integer leadId);
}