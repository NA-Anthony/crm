package site.easy.to.build.crm.service.budget;

import site.easy.to.build.crm.entity.Budget;
import java.util.List;

public interface BudgetService {
    List<Budget> getAllBudgets();
    Budget getBudgetById(Integer id);
    Budget createBudget(Budget budget);
    Budget updateBudget(Integer id, Budget budget);
    void deleteBudget(Integer id);
    Double getSoldeByCustomerId(Integer customerId);
}