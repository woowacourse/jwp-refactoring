package kitchenpos.domain.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.OrderLineItem;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findAllByOrderIdIn(Set<Long> orderId);
}
