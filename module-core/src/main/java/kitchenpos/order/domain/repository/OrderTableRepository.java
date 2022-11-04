package kitchenpos.order.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.order.domain.OrderTable;

public interface OrderTableRepository extends Repository<OrderTable, Long> {

    OrderTable save(final OrderTable orderTable);

    Optional<OrderTable> findById(final Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(final List<Long> ids);
}
