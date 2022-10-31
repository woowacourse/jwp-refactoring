package kitchenpos.domain.order.repository;

import kitchenpos.domain.order.OrderTable;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends Repository<OrderTable, Long> {

    OrderTable save(final OrderTable orderTable);

    Optional<OrderTable> findById(final Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(final List<Long> ids);
}
