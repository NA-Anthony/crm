package site.easy.to.build.crm.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.DTO.BudgetDtoCsv;
import site.easy.to.build.crm.DTO.CustomerDtoCsv;
import site.easy.to.build.crm.DTO.TicketLeadDtoCsv;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.taux.TauxAlerteService;
import site.easy.to.build.crm.service.user.UserService;

import java.sql.SQLDataException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@Service
public class DatabaseCustomUtil {

    private JdbcTemplate jdbcTemplate;
    private final Faker faker = new Faker();
    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;
    // service
    private UserService userService;
    private TauxAlerteService tauxAlerteService;

    @Transactional
    public void resetDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        // Liste des tables à supprimer
        List<String> tables = List.of(
                "contract_settings",
                "email_template",
                "employee",
                "file",
                "google_drive_file",
                "lead_action",
                "lead_settings",
                "ticket_settings",
                "trigger_lead",
                "trigger_ticket",
                "trigger_contract",
                "customer",
                "customer_login_info",
                "expense",
                "budget");
        tables.forEach(table -> {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        });
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }


    ////////////////// CSV IMPORTATION
    ////////////////// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<CustomerLoginInfo> buildCustomer(List<CustomerDtoCsv> customerDtoCsvs) {
        User admin = userService.findFirst();
        List<CustomerLoginInfo> customerLoginInfos = new ArrayList<>();
        for (CustomerDtoCsv customerDtoCsv : customerDtoCsvs) {
            Customer customer = new Customer();
            customer.setName(customerDtoCsv.getName());
            customer.setEmail(customerDtoCsv.getEmail());
            customer.setPosition(faker.job().position());
            customer.setCountry(faker.address().country());
            customer.setCity(faker.address().city());
            customer.setUser(admin);

            CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();
            customerLoginInfo.setEmail(customerDtoCsv.getEmail());
            customerLoginInfo.setPasswordSet(true);
            String hashPassword = passwordEncoder.encode("2004");
            customerLoginInfo.setPassword(hashPassword);
            String token = EmailTokenUtils.generateToken();
            customerLoginInfo.setToken(token);
            customerLoginInfo.setCustomer(customer);

            customerLoginInfos.add(customerLoginInfo);
        }
        return customerLoginInfos;
    }

    public List<Budget> buildBudget(List<BudgetDtoCsv> budgetDtoCsvs, HashMap<String, Integer> mapCustomer,
            StringBuilder errorMessage) {
        List<Budget> budgets = new ArrayList<>();
        List<String> errors = new ArrayList<>(); // Collect errors here

        for (int i = 0; i < budgetDtoCsvs.size(); i++) {
            BudgetDtoCsv budgetDtoCsv = budgetDtoCsvs.get(i);
            Budget budget = new Budget();
            Integer customerId = mapCustomer.get(budgetDtoCsv.getCustomerEmail());

            if (customerId == null) {
                errors.add(String.format("Row %d: Customer email '%s' not found for budget.", i + 2,
                        budgetDtoCsv.getCustomerEmail()));
            } else {
                Customer customer = new Customer();
                customer.setCustomerId(customerId);

                budget.setMontant(budgetDtoCsv.getBudget());
                budget.setCustomer(customer);
                budget.setTauxAlerte(tauxAlerteService.getLastTauxAlerte());

                budgets.add(budget);
            }
        }

        if (!errors.isEmpty()) {
            errorMessage.append("<ul>");
            errors.forEach(error -> errorMessage.append("<li>").append(error).append("</li>"));
            errorMessage.append("</ul>");
        }

        return budgets;
    }

    public List<Depense> buildTicket(List<TicketLeadDtoCsv> ticketLeadDtoCsvs, HashMap<String, Integer> mapCustomer,
            StringBuilder errorMessage) {
        User admin = userService.findFirst();
        List<Depense> depenses = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < ticketLeadDtoCsvs.size(); i++) {
            TicketLeadDtoCsv ticketLeadDtoCsv = ticketLeadDtoCsvs.get(i);
            if (ticketLeadDtoCsv.getType().equalsIgnoreCase("ticket")
                    || ticketLeadDtoCsv.getType().equalsIgnoreCase("tickets")) {
                Ticket ticket = new Ticket();
                Integer customerId = mapCustomer.get(ticketLeadDtoCsv.getCustomerEmail());

                if (customerId == null) {
                    errors.add(String.format("Row %d: Customer email '%s' not found for ticketLeadCsv.", i + 2,
                            ticketLeadDtoCsv.getCustomerEmail()));
                } else {
                    Depense expense = new Depense();
                    expense.setMontant(ticketLeadDtoCsv.getExpense());
                    expense.setDate(LocalDate.now());

                    Customer customer = new Customer();
                    customer.setCustomerId(customerId);

                    ticket.setSubject(ticketLeadDtoCsv.getSubjectOrName());
                    ticket.setDescription(faker.lorem().sentence());
                    ticket.setStatus(ticketLeadDtoCsv.getStatus());
                    ticket.setPriority("critical");
                    ticket.setCustomer(customer);
                    ticket.setManager(admin);
                    ticket.setEmployee(admin);
                    ticket.setCreatedAt(LocalDateTime.now());

                    expense.setTicket(ticket);
                    depenses.add(expense);
                }
            }
        }

        if (!errors.isEmpty()) {
            errorMessage.append("<ul>");
            errors.forEach(error -> errorMessage.append("<li>").append(error).append("</li>"));
            errorMessage.append("</ul>");
        }

        return depenses;
    }

    public List<Depense> buildLead(List<TicketLeadDtoCsv> ticketLeadDtoCsvs, HashMap<String, Integer> mapCustomer,
            StringBuilder errorMessage) {
        User admin = userService.findFirst();
        System.out.println(admin.getEmail());
        List<Depense> depenses = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < ticketLeadDtoCsvs.size(); i++) {
            TicketLeadDtoCsv ticketLeadDtoCsv = ticketLeadDtoCsvs.get(i);
            if (ticketLeadDtoCsv.getType().equalsIgnoreCase("lead")
                    || ticketLeadDtoCsv.getType().equalsIgnoreCase("leads")) {
                Lead lead = new Lead();
                Integer customerId = mapCustomer.get(ticketLeadDtoCsv.getCustomerEmail());

                if (customerId == null) {
                    errors.add(String.format("Row %d: Customer email '%s' not found for ticketLeadCsv.", i + 2,
                            ticketLeadDtoCsv.getCustomerEmail()));
                } else {
                    Depense expense = new Depense();
                    expense.setMontant(ticketLeadDtoCsv.getExpense());
                    expense.setDate(LocalDate.now());

                    Customer customer = new Customer();
                    customer.setCustomerId(customerId);

                    lead.setCustomer(customer);
                    lead.setManager(admin);
                    lead.setName(ticketLeadDtoCsv.getSubjectOrName());
                    lead.setEmployee(admin);
                    lead.setStatus(ticketLeadDtoCsv.getStatus());
                    lead.setCreatedAt(LocalDateTime.now());

                    expense.setLead(lead);

                    depenses.add(expense);
                }
            }
        }

        if (!errors.isEmpty()) {
            errorMessage.append("<ul>");
            errors.forEach(error -> errorMessage.append("<li>").append(error).append("</li>"));
            errorMessage.append("</ul>");
        }
        return depenses;
    }

    @Transactional(rollbackFor = SQLDataException.class)
    public void importCsvAndSave(List<BudgetDtoCsv> budgetDtoCsvs, List<TicketLeadDtoCsv> ticketLeadDtoCsvs,
            List<CustomerDtoCsv> customerDtoCsvs) throws SQLDataException {
        StringBuilder errorMessage = new StringBuilder();
        HashMap<String, Integer> mapCustomer = new HashMap<>();

        // 1. Sauvegarde des clients
        List<CustomerLoginInfo> customerLoginInfos = buildCustomer(customerDtoCsvs);
        for (CustomerLoginInfo customerLoginInfo : customerLoginInfos) {
            entityManager.persist(customerLoginInfo.getCustomer());
            entityManager.persist(customerLoginInfo);
            mapCustomer.put(customerLoginInfo.getEmail(), customerLoginInfo.getCustomer().getCustomerId());
        }

        // 2. Sauvegarde des budgets
        List<Budget> budgets = buildBudget(budgetDtoCsvs, mapCustomer, errorMessage);
        for (Budget budget : budgets) {
            entityManager.persist(budget);
        }

        // 3. Sauvegarde des tickets
        List<Depense> tickets = buildTicket(ticketLeadDtoCsvs, mapCustomer, errorMessage);
        for (Depense depense : tickets) {
            entityManager.persist(depense.getTicket());
            entityManager.persist(depense);
        }

        // 4. Sauvegarde des leads
        List<Depense> leads = buildLead(ticketLeadDtoCsvs, mapCustomer, errorMessage);
        for (Depense lead : leads) {
            entityManager.persist(lead.getLead());
            entityManager.persist(lead);
        }
        if (!errorMessage.isEmpty()) {
            throw new SQLDataException(errorMessage.toString());
        }
    }
}
