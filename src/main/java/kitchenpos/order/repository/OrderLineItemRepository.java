package kitchenpos.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.OrderLineItem;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
