package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.TauxAlerte;
import site.easy.to.build.crm.service.taux.TauxAlerteService;

import java.util.List;

@Controller
@RequestMapping("/taux-alertes")
public class TauxAlerteController {

    @Autowired
    private TauxAlerteService tauxAlerteService;

    // Afficher la liste des taux d'alerte
    @GetMapping
    public String getAllTauxAlertes(Model model) {
        List<TauxAlerte> tauxAlertes = tauxAlerteService.getAllTauxAlertes();
        model.addAttribute("tauxAlertes", tauxAlertes);
        return "taux-alertes/list"; // Retourne le nom de la vue (template)
    }

    // Afficher le formulaire de création d'un taux d'alerte
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("tauxAlerte", new TauxAlerte());
        return "taux-alertes/create"; // Retourne le nom de la vue (template)
    }

    // Traiter la création d'un taux d'alerte
    @PostMapping("/create")
    public String createTauxAlerte(@ModelAttribute TauxAlerte tauxAlerte) {
        tauxAlerteService.createTauxAlerte(tauxAlerte);
        return "redirect:/taux-alertes"; // Redirige vers la liste des taux d'alerte
    }

    // Afficher le formulaire de modification d'un taux d'alerte
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        TauxAlerte tauxAlerte = tauxAlerteService.getTauxAlerteById(id);
        model.addAttribute("tauxAlerte", tauxAlerte);
        return "taux-alertes/edit"; // Retourne le nom de la vue (template)
    }

    // Traiter la modification d'un taux d'alerte
    @PostMapping("/edit/{id}")
    public String updateTauxAlerte(@PathVariable Integer id, @ModelAttribute TauxAlerte tauxAlerte) {
        tauxAlerteService.updateTauxAlerte(id, tauxAlerte);
        return "redirect:/taux-alertes"; // Redirige vers la liste des taux d'alerte
    }

    // Supprimer un taux d'alerte
    @GetMapping("/delete/{id}")
    public String deleteTauxAlerte(@PathVariable Integer id) {
        tauxAlerteService.deleteTauxAlerte(id);
        return "redirect:/taux-alertes"; // Redirige vers la liste des taux d'alerte
    }
}