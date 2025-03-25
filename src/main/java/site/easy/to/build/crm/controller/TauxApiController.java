package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.TauxAlerte;
import site.easy.to.build.crm.service.taux.TauxAlerteService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/taux")
public class TauxApiController {

    private final TauxAlerteService tauxAlerteService;
    @Autowired
    public TauxApiController(TauxAlerteService tauxAlerteService) {
        this.tauxAlerteService = tauxAlerteService;
    }

    @PostMapping("/create")
    public ResponseEntity<TauxAlerte> createTauxAlerte(@RequestBody Map<String, Double> requestBody) {
        double valeur = requestBody.get("valeur");

        TauxAlerte tauxAlerte = new TauxAlerte();
        tauxAlerte.setTaux(valeur);
        tauxAlerteService.createTauxAlerte(tauxAlerte);

        return ResponseEntity.ok(tauxAlerte);
    }
}