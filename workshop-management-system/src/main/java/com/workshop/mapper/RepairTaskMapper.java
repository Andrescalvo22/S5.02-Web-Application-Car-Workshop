package com.workshop.mapper;

import com.workshop.dto.RepairTaskDTO;
import com.workshop.model.RepairTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepairTaskMapper {

    @Mapping(source = "repairOrder.id", target = "repairOrderId")
    RepairTaskDTO toDTO(RepairTask task);

    @Mapping(source = "repairOrderId", target = "repairOrder.id")
    RepairTask toEntity(RepairTaskDTO dto);
}
