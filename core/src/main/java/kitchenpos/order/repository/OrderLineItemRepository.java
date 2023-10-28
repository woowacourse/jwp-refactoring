package kitchenpos.order.repository;

import kitchenpos.order.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findAllByOrderId(Long orderId);

    @Query(value = "select oli from OrderLineItem oli " +
            "where oli.orderMenu.id in :menuIds")
    List<OrderLineItem> findAllByMenuIds(@Param("menuIds") List<Long> menuIds);
}
