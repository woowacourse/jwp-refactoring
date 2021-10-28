package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuDao extends JpaRepository<Menu, Long> {
    Menu save(Menu entity);

    long countByOrderLineItemsIn(List<OrderLineItem> orderLineItems);
}
