package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.contract.ContractService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.taux.TauxAlerteService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    private final TicketService ticketService;
    private final CustomerService customerService;
    private final ContractService contractService;
    private final LeadService leadService;
    private final DepenseService depenseService;
    private final BudgetService budgetService;
    private final TauxAlerteService tauxAlerteService;

    @Autowired
    public DashboardApiController(TicketService ticketService, CustomerService customerService, ContractService contractService,
                                  LeadService leadService, DepenseService depenseService, BudgetService budgetService, TauxAlerteService tauxAlerteService) {
        this.ticketService = ticketService;
        this.customerService = customerService;
        this.contractService = contractService;
        this.leadService = leadService;
        this.depenseService = depenseService;
        this.budgetService = budgetService;
        this.tauxAlerteService = tauxAlerteService;
    }

    @GetMapping("/data")
    public ResponseEntity<?> getDashboardData(@RequestParam int userId) {
        List<Ticket> tickets = ticketService.getRecentEmployeeTickets(userId, 10);
        List<Lead> leads = leadService.getRecentLeadsByEmployee(userId, 10);
        List<Customer> customers = customerService.getRecentCustomers(userId, 10);
        List<Contract> contracts = contractService.getRecentContracts(userId, 10);
        List<Double> depenses = new ArrayList<>();
        List<Double> budgets = new ArrayList<>();
        List<Double> depensesTicket = new ArrayList<>();
        List<Double> depensesLead = new ArrayList<>();
        TauxAlerte tauxAlerte = tauxAlerteService.getLastTauxAlerte();
        Double depense = 0.0;
        Double sommeTicket = 0.0;
        Double sommeLead = 0.0;

        for (Customer customer:customers){
            depense = depenseService.getTotalDepensesByCustomerId(customer.getCustomerId());
            if (depense != null){
                depenses.add(depense);
            }
            else {
                depenses.add(0.0);
            }
            budgets.add(budgetService.getSoldeByCustomerId(customer.getCustomerId()));
        }

        for (Ticket ticket : tickets){
            depense = depenseService.findByTicketId(ticket.getTicketId()).getMontant();
            depensesTicket.add(depense);
            sommeTicket += depense;
        }

        for (Lead lead : leads){
            depense = depenseService.findByLeadId(lead.getLeadId()).getMontant();
            depensesLead.add(depense);
            sommeLead += depense;
        }

        int countTickets = tickets.size();
        int countLeads = leads.size();
        int countContracts = contracts.size();
        int countCustomers = customers.size();

        // Logique pour récupérer les données du dashboard
        Map<String, Object> data = new HashMap<>();
        data.put("tickets", tickets);
        data.put("leads", leads);
        data.put("contracts", contracts);
        data.put("customers", customers);
        data.put("countTickets", countTickets);
        data.put("countLeads", countLeads);
        data.put("countContracts", countContracts);
        data.put("countCustomers", countCustomers);
        data.put("depenses", depenses);
        data.put("budgets", budgets);
        data.put("taux",tauxAlerte);
        data.put("depensesLead",depensesLead);
        data.put("depensesTicket",depensesTicket);
        data.put("sommeLead",sommeLead);
        data.put("sommeTicket",sommeTicket);

        return ResponseEntity.ok(data);
    }
}