package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.OrderLineItem;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findAllByOrderId(Long orderId);
}
