package com.workshop.mapper;

import com.workshop.dto.CustomerDTO;
import com.workshop.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDto(Customer customer);

    Customer toEntity(CustomerDTO customerDTO);
}
