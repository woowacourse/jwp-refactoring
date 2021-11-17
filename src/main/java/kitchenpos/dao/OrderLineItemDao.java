package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemDao extends JpaRepository<OrderLineItem, Long> {
//    OrderLineItem save(OrderLineItem entity);
//
//    Optional<OrderLineItem> findById(Long id);
//
//    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
