package kitchenpos.order.persistence;

import kitchenpos.order.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findAllByOrderId(final Long orderId);
}
