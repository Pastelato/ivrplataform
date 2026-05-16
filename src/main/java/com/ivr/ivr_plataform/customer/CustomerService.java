package com.ivr.ivr_plataform.customer;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public Customer create(Customer customer) {
        return repo.save(customer);
    }

    public List<Customer> getAll() {
        return repo.findAll();
    }
}
