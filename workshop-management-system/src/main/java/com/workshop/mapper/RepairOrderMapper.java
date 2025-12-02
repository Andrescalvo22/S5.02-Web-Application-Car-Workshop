package com.workshop.mapper;

import com.workshop.dto.RepairOrderDTO;
import com.workshop.model.RepairOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepairOrderMapper {

    @Mapping(source = "car.id", target = "carId")
    RepairOrderDTO toDTO(RepairOrder repairOrder);

    @Mapping(source = "carId", target = "car.id")
    RepairOrder toEntity(RepairOrderDTO dto);
}
