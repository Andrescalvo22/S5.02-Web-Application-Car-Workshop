package com.workshop.repository;

import com.workshop.model.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {
    List<RepairOrder> findByCarCustomerId(Long customerId);

}
