package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.TauxAlerte;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.data.DataGeneratorService;
import site.easy.to.build.crm.service.taux.TauxAlerteService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.AuthorizationUtil;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/budgets")
public class BudgetController {

    private final AuthenticationUtils authenticationUtils;
    private final UserService userService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TauxAlerteService tauxAlerteService;

    @Autowired
    private DataGeneratorService dataGeneratorService;

    public BudgetController(AuthenticationUtils authenticationUtils, UserService userService) {
        this.authenticationUtils = authenticationUtils;
        this.userService = userService;
    }

    // Afficher la liste des budgets
    @GetMapping
    public String getAllBudgets(Model model) {
        List<Budget> budgets = budgetService.getAllBudgets();
        model.addAttribute("budgets", budgets);
        return "budgets/list";
    }

    // Afficher le formulaire de création d'un budget
    @GetMapping("/create")
    public String showCreateForm(Model model, Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        List<Customer> customers = customerService.findAll();
        if(AuthorizationUtil.hasRole(authentication, "ROLE_EMPLOYEE")) {
            customers = customerService.findByUserId(userId);
        }
        model.addAttribute("budget", new Budget());
        model.addAttribute("customers", customers);

        // Récupérer tous les taux d'alerte et les ajouter au modèle
        List<TauxAlerte> tauxAlertes = new ArrayList<>();
        TauxAlerte tauxAlerte = tauxAlerteService.getLastTauxAlerte();
        tauxAlertes.add(tauxAlerte);
        model.addAttribute("tauxAlertes", tauxAlertes);

        return "budgets/create";
    }

    // Traiter la création d'un budget
    @PostMapping("/create")
    public String createBudget(@ModelAttribute Budget budget) {
        budgetService.createBudget(budget);
        return "redirect:/budgets";
    }

    // Afficher le formulaire de modification d'un budget
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        // Récupérer le budget à modifier
        Budget budget = budgetService.getBudgetById(id);
        model.addAttribute("budget", budget);

        // Récupérer tous les clients et les ajouter au modèle
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);

        // Récupérer tous les taux d'alerte et les ajouter au modèle
        List<TauxAlerte> tauxAlertes = tauxAlerteService.getAllTauxAlertes();
        model.addAttribute("tauxAlertes", tauxAlertes);

        return "budgets/edit";
    }

    // Traiter la modification d'un budget
    @PostMapping("/edit/{id}")
    public String updateBudget(@PathVariable Integer id, @ModelAttribute Budget budget) {
        budgetService.updateBudget(id, budget);
        return "redirect:/budgets";
    }

    // Supprimer un budget
    @GetMapping("/delete/{id}")
    public String deleteBudget(@PathVariable Integer id) {
        budgetService.deleteBudget(id);
        return "redirect:/budgets";
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
        return "budgets/generate-random";
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
            dataGeneratorService.generateRandomBudget(numberOfTaux);

            // Ajouter un message de succès
            redirectAttributes.addFlashAttribute("successMessage", "Successfully generated " + numberOfTaux + " random customers.");
        } catch (Exception e) {
            // Ajouter un message d'erreur en cas d'échec
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to generate random customers: " + e.getMessage());
        }

        return "redirect:/";
    }
}