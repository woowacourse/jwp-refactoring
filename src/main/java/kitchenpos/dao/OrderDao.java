package kitchenpos.dao;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDao extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderTable(OrderTable orderTable);
}
