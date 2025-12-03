package com.workshop.mapper;

import com.workshop.dto.RepairTaskDTO;
import com.workshop.model.RepairOrder;
import com.workshop.model.RepairTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RepairTaskMapper {

    @Mapping(source = "repairOrder.id", target = "repairOrderId")
    RepairTaskDTO toDTO(RepairTask task);

    @Mapping(target = "repairOrder", expression = "java(toRepairOrder(dto.getRepairOrderId()))")
    RepairTask toEntity(RepairTaskDTO dto);

    default RepairOrder toRepairOrder(Long id) {
        if (id == null) return null;
        RepairOrder order = new RepairOrder();
        order.setId(id);
        return order;
    }
}

