package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerApiController {

    private final CustomerService customerService;
    @Autowired
    public CustomerApiController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Récupère tous les customers.
     *
     * @return Une liste de tous les customers.
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllTickets(@RequestParam int userId) {
        List<Customer> customers = customerService.findByUserId(userId);
        return ResponseEntity.ok(customers);
    }
}