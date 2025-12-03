package com.workshop.mapper;

import com.workshop.dto.RepairOrderDTO;
import com.workshop.model.Car;
import com.workshop.model.RepairOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepairOrderMapper {

    @Mapping(source = "car.id", target = "carId")
    RepairOrderDTO toDTO(RepairOrder order);

    @Mapping(target = "car", expression = "java(toCar(dto.getCarId()))")
    RepairOrder toEntity(RepairOrderDTO dto);

    default Car toCar(Long id) {
        if (id == null) return null;
        Car car = new Car();
        car.setId(id);
        return car;
    }
}

