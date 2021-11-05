package kitchenpos.domain.orderlineitem;

import java.util.List;
import kitchenpos.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findAllByOrder(Order order);
}
