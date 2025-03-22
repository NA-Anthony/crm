package site.easy.to.build.crm.service.taux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.TauxAlerte;
import site.easy.to.build.crm.repository.TauxAlerteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TauxAlerteServiceImpl implements TauxAlerteService {

    @Autowired
    private TauxAlerteRepository tauxAlerteRepository;

    @Override
    public List<TauxAlerte> getAllTauxAlertes() {
        return tauxAlerteRepository.findAll();
    }

    @Override
    public TauxAlerte getTauxAlerteById(Integer id) {
        Optional<TauxAlerte> optionalTauxAlerte = tauxAlerteRepository.findById(id);
        return optionalTauxAlerte.orElseThrow(() -> new RuntimeException("TauxAlerte not found with id: " + id));
    }

    @Override
    public TauxAlerte createTauxAlerte(TauxAlerte tauxAlerte) {
        return tauxAlerteRepository.save(tauxAlerte);
    }

    @Override
    public TauxAlerte updateTauxAlerte(Integer id, TauxAlerte tauxAlerte) {
        TauxAlerte existingTauxAlerte = getTauxAlerteById(id);
        existingTauxAlerte.setTaux(tauxAlerte.getTaux());
        return tauxAlerteRepository.save(existingTauxAlerte);
    }

    @Override
    public void deleteTauxAlerte(Integer id) {
        tauxAlerteRepository.deleteById(id);
    }

    @Override
    public TauxAlerte getLastTauxAlerte() {
        return tauxAlerteRepository.findLastTauxAlerte();
    }
}
