package kitchenpos.repository;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    long countByOrderLineItemsIn(List<OrderLineItem> orderLineItems);
}
