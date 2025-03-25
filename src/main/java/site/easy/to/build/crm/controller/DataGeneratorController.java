package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.data.DataGeneratorService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;

@Controller
@RequestMapping("/generate")
public class DataGeneratorController {

    @Autowired
    private DataGeneratorService dataGeneratorService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationUtils authenticationUtils;

    // Afficher le formulaire
    @GetMapping("/generate-random-data")
    public String showGenerateForm(Model model, Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if (user.isInactiveUser()) {
            return "error/account-inactive";
        }

        model.addAttribute("dataCount", 5); // Valeur par défaut
        return "data/generator-form";
    }

    // Traiter la génération
    @PostMapping("/generate-random-data")
    public String generateAllData(
            @RequestParam("dataCount") int count,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if (user.isInactiveUser()) {
            return "error/account-inactive";
        }

        try {
            // Générer toutes les données
            dataGeneratorService.generateRandomTaux(count);
            dataGeneratorService.generateRandomCustomers(count, authentication);
            dataGeneratorService.generateRandomTicket(count,authentication);
            dataGeneratorService.generateRandomLead(count,authentication);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Successfully generated " + count + " of each data type.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error during data generation: " + e.getMessage());
        }

        return "redirect:/";
    }
}