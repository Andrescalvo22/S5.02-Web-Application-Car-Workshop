package com.workshop.mapper;

import com.workshop.dto.CarDTO;
import com.workshop.model.Car;
import com.workshop.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(source = "customer.id", target = "customerId")
    CarDTO toDTO(Car car);

    @Mapping(target = "customer", expression = "java(toCustomer(dto.getCustomerId()))")
    Car toEntity(CarDTO dto);

    default Customer toCustomer(Long id) {
        if (id == null) return null;
        Customer c = new Customer();
        c.setId(id);
        return c;
    }
}
