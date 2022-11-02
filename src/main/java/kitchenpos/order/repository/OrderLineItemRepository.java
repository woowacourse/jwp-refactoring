package kitchenpos.order.repository;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
