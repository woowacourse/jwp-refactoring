package kitchenpos.table.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.table.domain.OrderTable;

public interface OrderTableRepository extends Repository<OrderTable, Long> {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAllByIdIn(List<Long> id);

    List<OrderTable> findAll();

    List<OrderTable> findByTableGroupId(Long tableGroupId);

}
