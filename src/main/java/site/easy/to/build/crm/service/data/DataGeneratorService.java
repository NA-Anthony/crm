package site.easy.to.build.crm.service.data;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.google.service.gmail.GoogleGmailApiService;
import site.easy.to.build.crm.repository.CustomerLoginInfoRepository;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.taux.TauxAlerteService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.EmailTokenUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class DataGeneratorService {

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerLoginInfoRepository customerLoginInfoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GoogleGmailApiService googleGmailApiService;

    @Autowired
    private AuthenticationUtils authenticationUtils;

    @Autowired
    private Environment environment;

    @Autowired
    private TauxAlerteService tauxAlerteService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LeadService leadService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private DepenseService depenseService;

    @Autowired
    private BudgetService budgetService;

    public DataGeneratorService(CustomerRepository customerRepository,
                                CustomerLoginInfoRepository customerLoginInfoRepository,
                                UserService userService) {
        this.customerRepository = customerRepository;
        this.customerLoginInfoRepository = customerLoginInfoRepository;
        this.userService = userService;
    }

    // Générer et sauvegarder un customer aléatoire
    public void generateRandomCustomer(Authentication authentication) {
        // Récupérer tous les utilisateurs et en sélectionner un au hasard
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            throw new IllegalStateException("No users found in database. Cannot generate customers.");
        }
        User randomUser = users.get(random.nextInt(users.size()));

        // Créer un nouveau customer
        Customer customer = new Customer();
        customer.setName(faker.name().fullName());
        customer.setPhone(faker.phoneNumber().cellPhone());
        customer.setAddress(faker.address().fullAddress());
        customer.setCity(faker.address().city());
        customer.setState(faker.address().state());
        customer.setCountry(faker.address().country());
        customer.setDescription(faker.lorem().paragraph());
        customer.setPosition(faker.job().position());
        customer.setTwitter(faker.internet().url());
        customer.setFacebook(faker.internet().url());
        customer.setYoutube(faker.internet().url());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setEmail(faker.internet().emailAddress());
        customer.setUser(randomUser);

        // Générer et sauvegarder les informations de connexion du customer
        CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();
        customerLoginInfo.setEmail(customer.getEmail());
        customerLoginInfo.setUsername(faker.name().username());
        customerLoginInfo.setToken(faker.internet().uuid());
        customerLoginInfo.setPasswordSet(random.nextBoolean());
        CustomerLoginInfo customerLoginInfo1 = customerLoginInfoRepository.save(customerLoginInfo);// Assigner l'utilisateur aléatoire

        customer.setCustomerLoginInfo(customerLoginInfo1);
        Customer savedCustomer = customerRepository.save(customer);

        customerLoginInfo1.setCustomer(savedCustomer);

        // Envoyer un email de bienvenue
        sendRegistrationEmail(savedCustomer, customerLoginInfo1, authentication);
    }

    public void generateRandomCustomerForUser(int id,Authentication authentication) {

        // Créer un nouveau customer
        Customer customer = new Customer();
        customer.setName(faker.name().fullName());
        customer.setPhone(faker.phoneNumber().cellPhone());
        customer.setAddress(faker.address().fullAddress());
        customer.setCity(faker.address().city());
        customer.setState(faker.address().state());
        customer.setCountry(faker.address().country());
        customer.setDescription(faker.lorem().paragraph());
        customer.setPosition(faker.job().position());
        customer.setTwitter(faker.internet().url());
        customer.setFacebook(faker.internet().url());
        customer.setYoutube(faker.internet().url());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setEmail(faker.internet().emailAddress());
        customer.setUser(userService.findById(id));

        // Générer et sauvegarder les informations de connexion du customer
        CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();
        customerLoginInfo.setEmail(customer.getEmail());
        customerLoginInfo.setUsername(faker.name().username());
        customerLoginInfo.setToken(faker.internet().uuid());
        customerLoginInfo.setPasswordSet(random.nextBoolean());
        CustomerLoginInfo customerLoginInfo1 = customerLoginInfoRepository.save(customerLoginInfo);// Assigner l'utilisateur aléatoire

        customer.setCustomerLoginInfo(customerLoginInfo1);
        Customer savedCustomer = customerRepository.save(customer);

        customerLoginInfo1.setCustomer(savedCustomer);

        generateRandomBudget(savedCustomer.getCustomerId());

        sendRegistrationEmail(savedCustomer, customerLoginInfo1, authentication);
    }

    // Générer plusieurs customers aléatoires
    public void generateRandomCustomers(int numberOfCustomers, Authentication authentication) {
        for (int i = 0; i < numberOfCustomers; i++) {
            generateRandomCustomer(authentication);
        }
    }

    // Envoyer un email de bienvenue
    private void sendRegistrationEmail(Customer customer, CustomerLoginInfo customerLoginInfo, Authentication authentication) {
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            OAuthUser oAuthUser = authenticationUtils.getOAuthUserFromAuthentication(authentication);
            String baseUrl = environment.getProperty("app.base-url");
            String url = baseUrl + "set-password?token=" + customerLoginInfo.getToken();
            EmailTokenUtils.sendRegistrationEmail(customerLoginInfo.getEmail(), customer.getName(), url, oAuthUser, googleGmailApiService);
        }
    }

    public void generateRandomTaux(int number) {
        for (int i = 0; i < number; i++) {
            TauxAlerte tauxAlerte = new TauxAlerte();
            // Générer un taux aléatoire entre 0.00 et 100.00 inclus avec 2 décimales
            double taux = (double) random.nextInt(100);
            tauxAlerte.setTaux(taux);
            tauxAlerteService.createTauxAlerte(tauxAlerte);
        }
    }

    public void generateRandomBudget(int id) {
        TauxAlerte tauxAlerte = tauxAlerteService.getLastTauxAlerte();
        Budget budget = new Budget();
        Customer randomCustomer = customerService.findByCustomerId(id);
        budget.setCustomer(randomCustomer);
        budget.setMontant((double) random.nextInt(1000001));
        budget.setTauxAlerte(tauxAlerte);
        budgetService.createBudget(budget);
    }

    public void generateRandomTicket(int number,Authentication authentication) {
        for (int i = 0; i < number; i++) {
            Ticket ticket = new Ticket();
            ticket.setSubject("ticket");
            ticket.setDescription(faker.lorem().paragraph());
            ticket.setStatus(getRandomStatus());
            ticket.setPriority(getRandomPriority());
            List<User> users = userService.findAll();
            User randomUser1 = users.get(random.nextInt(users.size()));
            User randomUser2 = users.get(random.nextInt(users.size()));
            ticket.setEmployee(randomUser1);
            ticket.setManager(randomUser2);
            ticket.setCreatedAt(LocalDateTime.now());
            List<Customer> customers = customerService.findByUserId(randomUser1.getId());
            if (customers.isEmpty()){
                generateRandomCustomerForUser(randomUser1.getId(),authentication);
                customers = customerService.findByUserId(randomUser1.getId());
            }
            Customer randomCustomer = customers.get(random.nextInt(customers.size()));
            ticket.setCustomer(randomCustomer);
            Ticket ticket1 = ticketService.save(ticket);
            Depense depense = new Depense();
            depense.setTicket(ticket1);
            depense.setMontant((double) random.nextInt(1000001));
            depense.setDate(LocalDate.now());

            depenseService.createDepense(depense);
        }
    }

    public void generateRandomLead(int number,Authentication authentication) {
        for (int i = 0; i < number; i++) {
            Lead lead = new Lead();
            lead.setName("lead");
            lead.setPhone(faker.phoneNumber().cellPhone());
            lead.setStatus(getRandomStatusLead());
            lead.setCreatedAt(LocalDateTime.now());
            List<User> users = userService.findAll();
            User randomUser1 = users.get(random.nextInt(users.size()));
            User randomUser2 = users.get(random.nextInt(users.size()));
            lead.setEmployee(randomUser1);
            lead.setManager(randomUser2);
            List<Customer> customers = customerService.findByUserId(randomUser1.getId());
            if (customers.isEmpty()){
                generateRandomCustomerForUser(randomUser1.getId(),authentication);
                customers = customerService.findByUserId(randomUser1.getId());
            }
            Customer randomCustomer = customers.get(random.nextInt(customers.size()));
            lead.setCustomer(randomCustomer);

            Lead lead1 = leadService.save(lead);
            Depense depense = new Depense();
            depense.setLead(lead1);
            depense.setMontant((double) random.nextInt(1000001));
            depense.setDate(LocalDate.now());

            depenseService.createDepense(depense);
        }
    }

    private String getRandomStatus() {
        String[] statuses = {
                "open",
                "assigned",
                "on-hold",
                "in-progress",
                "resolved",
                "closed",
                "reopened",
                "pending-customer-response",
                "escalated",
                "archived"
        };

        return statuses[random.nextInt(statuses.length)];
    }

    private String getRandomStatusLead() {
        String[] statuses = {
                "meeting-to-schedule",
                "assign-to-sales",
                "archived",
                "success"
        };

        return statuses[random.nextInt(statuses.length)];
    }

    private String getRandomPriority() {
        String[] statuses = {
                "low",
                "medium",
                "high",
                "closed",
                "urgent",
                "critical"
        };

        return statuses[random.nextInt(statuses.length)];
    }
}