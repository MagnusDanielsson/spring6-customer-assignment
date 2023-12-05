package edu.springframework.spring6customerassignment.mappers;

import edu.springframework.spring6customerassignment.entities.Customer;
import edu.springframework.spring6customerassignment.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDto(Customer customer);
}
