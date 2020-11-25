package kitchenpos.order.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.table.domain.Table;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByTableAndOrderStatusIn(Table table, List<OrderStatus> orderStatuses);
}
