package edu.springframework.spring6customerassignment.bootstrap;

import edu.springframework.spring6customerassignment.entities.Customer;
import edu.springframework.spring6customerassignment.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
@Profile(value = {"default", "dev"})
public class BootstrapData implements CommandLineRunner {
    CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        loadCustomerData();
    }
    private void loadCustomerData() {
        if (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                    .customerName("John Doe")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer2 = Customer.builder()
                    .customerName("Donald Trump")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer3 = Customer.builder()
                    .customerName("Magnus Danielsson")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer4 = Customer.builder()
                    .customerName("The King")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customer1= customerRepository.save(customer1);
            log.info("Customer entity #1 " + customer1.getCustomerName());

            customer2= customerRepository.save(customer2);
            log.info("Customer entity #2 " + customer2.getCustomerName());

            customer3= customerRepository.save(customer3);
            log.info("Customer entity #3 " + customer3.getCustomerName());

            customer4= customerRepository.save(customer4);
            log.info("Customer entity #4 " + customer4.getCustomerName());

            List<Customer> customerList = customerRepository.findAll();
            customerList.forEach(c -> System.out.println("Customer name -> " + c.toString()));

        }

    }

}
