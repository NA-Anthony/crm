package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.TauxAlerte;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.taux.TauxAlerteService;

import java.util.List;

@Controller
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TauxAlerteService tauxAlerteService;

    // Afficher la liste des budgets
    @GetMapping
    public String getAllBudgets(Model model) {
        List<Budget> budgets = budgetService.getAllBudgets();
        model.addAttribute("budgets", budgets);
        return "budgets/list";
    }

    // Afficher le formulaire de création d'un budget
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Ajouter un nouvel objet Budget au modèle
        model.addAttribute("budget", new Budget());

        // Récupérer tous les clients et les ajouter au modèle
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);

        // Récupérer tous les taux d'alerte et les ajouter au modèle
        List<TauxAlerte> tauxAlertes = tauxAlerteService.getAllTauxAlertes();
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
}