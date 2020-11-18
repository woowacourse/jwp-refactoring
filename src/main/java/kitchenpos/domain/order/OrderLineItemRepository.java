package kitchenpos.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    public List<OrderLineItem> findAllByOrder(Order order);
}
