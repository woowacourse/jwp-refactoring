package kitchenpos.Order.domain.repository;

import kitchenpos.Order.domain.Order;
import kitchenpos.Order.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findAllByOrder(Order order);
}
