package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.OrderLineItem;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
