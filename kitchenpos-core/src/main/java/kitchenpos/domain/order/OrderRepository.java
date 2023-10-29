package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends Repository<Order, Long> {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableAndOrderStatusIn(AggregateReference<OrderTable, Long> orderTable, List<OrderStatus> orderStatuses);
}
