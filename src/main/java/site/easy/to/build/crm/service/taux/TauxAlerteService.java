package site.easy.to.build.crm.service.taux;

import site.easy.to.build.crm.entity.TauxAlerte;

import java.util.List;

public interface TauxAlerteService {
    List<TauxAlerte> getAllTauxAlertes();
    TauxAlerte getTauxAlerteById(Integer id);
    TauxAlerte createTauxAlerte(TauxAlerte tauxAlerte);
    TauxAlerte updateTauxAlerte(Integer id, TauxAlerte tauxAlerte);
    void deleteTauxAlerte(Integer id);
}