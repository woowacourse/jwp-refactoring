package kitchenpos.repository.order;

import kitchenpos.domain.order.OrderLineItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    @EntityGraph(attributePaths = "order")
    List<OrderLineItem> findAll();
}
