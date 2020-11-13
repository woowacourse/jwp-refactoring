package kitchenpos.order.repository;

import kitchenpos.order.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemDao extends JpaRepository<OrderLineItem, Long> {

}
