package com.workshop.mapper;

import com.workshop.dto.RepairOrderDTO;
import com.workshop.model.RepairOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { CarMapper.class })
public interface RepairOrderMapper {

    @Mapping(source = "car", target = "car")
    RepairOrderDTO toDTO(RepairOrder order);

    @Mapping(target = "car", ignore = true)
    RepairOrder toEntity(RepairOrderDTO dto);
}
