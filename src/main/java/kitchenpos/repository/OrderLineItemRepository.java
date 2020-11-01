package kitchenpos.repository;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Orderz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findAllByOrder(Orderz order);
}
