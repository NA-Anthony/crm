package site.easy.to.build.crm.service.data;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.OAuthUser;
import site.easy.to.build.crm.google.service.gmail.GoogleGmailApiService;
import site.easy.to.build.crm.repository.CustomerLoginInfoRepository;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.EmailTokenUtils;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class DataGeneratorService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerLoginInfoRepository customerLoginInfoRepository;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Autowired
    private GoogleGmailApiService googleGmailApiService;

    @Autowired
    private AuthenticationUtils authenticationUtils;

    @Autowired
    private Environment environment;

    public DataGeneratorService(CustomerRepository customerRepository, CustomerLoginInfoRepository customerLoginInfoRepository) {
        this.customerRepository = customerRepository;
        this.customerLoginInfoRepository = customerLoginInfoRepository;
    }

    // Générer et sauvegarder un customer aléatoire
    public void generateRandomCustomer(Authentication authentication) {
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

        // Sauvegarder le customer
        Customer savedCustomer = customerRepository.save(customer);

        // Générer et sauvegarder les informations de connexion du customer
        CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();
        customerLoginInfo.setEmail(savedCustomer.getEmail());
        customerLoginInfo.setPassword(faker.internet().password());
        customerLoginInfo.setUsername(faker.name().username());
        customerLoginInfo.setToken(faker.internet().uuid());
        customerLoginInfo.setPasswordSet(random.nextBoolean());

        customerLoginInfo.setCustomer(savedCustomer);
        customerLoginInfoRepository.save(customerLoginInfo);

        // Envoyer un email de bienvenue
        sendRegistrationEmail(savedCustomer, customerLoginInfo, authentication);
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
}