package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.TauxAlerte;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.data.DataGeneratorService;
import site.easy.to.build.crm.service.taux.TauxAlerteService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.util.List;

@Controller
@RequestMapping("/taux-alertes")
public class TauxAlerteController {

    @Autowired
    private TauxAlerteService tauxAlerteService;

    @Autowired
    private AuthenticationUtils authenticationUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private DataGeneratorService dataGeneratorService;

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

    @GetMapping("/generate-random")
    public String showGenerateRandomCustomersForm(Model model, Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if (user.isInactiveUser()) {
            return "error/account-inactive";
        }

        // Ajouter un attribut pour le formulaire
        model.addAttribute("numberOfTaux", 1); // Valeur par défaut
        return "taux-alertes/generate-random";
    }

    @PostMapping("/generate-random")
    public String generateRandomCustomers(
            @RequestParam("numberOfTaux") int numberOfTaux,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if (user.isInactiveUser()) {
            return "error/account-inactive";
        }

        try {
            // Appeler le service pour générer les clients
            dataGeneratorService.generateRandomTaux(numberOfTaux);

            // Ajouter un message de succès
            redirectAttributes.addFlashAttribute("successMessage", "Successfully generated " + numberOfTaux + " random customers.");
        } catch (Exception e) {
            // Ajouter un message d'erreur en cas d'échec
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to generate random customers: " + e.getMessage());
        }

        return "redirect:/";
    }
}