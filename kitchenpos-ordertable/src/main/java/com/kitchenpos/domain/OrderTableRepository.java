package com.kitchenpos.domain;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends Repository<OrderTable, Long> {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    boolean existsByIdAndEmptyIsFalse(Long orderTableId);

    boolean existsById(Long orderTableId);
}
