package kitchenpos.repository;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
