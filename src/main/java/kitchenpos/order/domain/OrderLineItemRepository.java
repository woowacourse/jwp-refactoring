package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    @Query("select ol from OrderLineItem ol where ol.order.id = :orderId")
    List<OrderLineItem> findAllByOrderId(Long orderId);
}
