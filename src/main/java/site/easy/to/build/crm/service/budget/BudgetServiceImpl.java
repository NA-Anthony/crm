package site.easy.to.build.crm.service.budget;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    @Override
    public Budget getBudgetById(Integer id) {
        Optional<Budget> optionalBudget = budgetRepository.findById(id);
        return optionalBudget.orElseThrow(() -> new RuntimeException("Budget not found with id: " + id));
    }

    @Override
    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public Budget updateBudget(Integer id, Budget budget) {
        Budget existingBudget = getBudgetById(id);
        existingBudget.setMontant(budget.getMontant());
        existingBudget.setTauxAlerte(budget.getTauxAlerte());
        existingBudget.setCustomer(budget.getCustomer());
        return budgetRepository.save(existingBudget);
    }

    @Override
    public void deleteBudget(Integer id) {
        budgetRepository.deleteById(id);
    }

    @Override
    public Double getSoldeByCustomerId(Integer customerId) {
        Double solde = budgetRepository.getTotalBudgetByCustomerId(customerId);
        return solde != null ? solde : 0.0; // Retourne 0.0 si aucun budget n'est trouvé
    }
}