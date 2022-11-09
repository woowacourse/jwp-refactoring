package kitchenpos.order.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.OrderLineItem;

public interface JpaOrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
