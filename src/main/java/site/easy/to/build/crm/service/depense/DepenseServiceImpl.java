package site.easy.to.build.crm.service.depense;

import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.repository.DepenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepenseServiceImpl implements DepenseService {

    @Autowired
    private DepenseRepository depenseRepository;

    @Override
    public List<Depense> getAllDepenses() {
        return depenseRepository.findAll();
    }

    @Override
    public Depense getDepenseById(Integer id) {
        Optional<Depense> optionalDepense = depenseRepository.findById(id);
        return optionalDepense.orElseThrow(() -> new RuntimeException("Dépense non trouvée avec l'ID : " + id));
    }

    @Override
    public Depense createDepense(Depense depense) {
        return depenseRepository.save(depense);
    }

    @Override
    public Depense updateDepense(Integer id, Depense depense) {
        Depense existingDepense = getDepenseById(id);
        existingDepense.setMontant(depense.getMontant());
        existingDepense.setTicket(depense.getTicket());
        existingDepense.setLead(depense.getLead());
        return depenseRepository.save(existingDepense);
    }

    @Override
    public void deleteDepense(Integer id) {
        depenseRepository.deleteById(id);
    }
}