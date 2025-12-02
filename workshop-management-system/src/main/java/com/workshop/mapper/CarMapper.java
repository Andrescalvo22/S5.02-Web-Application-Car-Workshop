package com.workshop.mapper;

import com.workshop.dto.CarDTO;
import com.workshop.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(source = "customer.id", target = "customerId")
    CarDTO toDTO(Car car);

    @Mapping(source = "customerId", target = "customer.id")
    Car toEntity(CarDTO dto);
}
