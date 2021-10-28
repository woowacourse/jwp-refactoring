package kitchenpos.domain.repository;

import kitchenpos.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("select ol from OrderItem ol where ol.orders.id = :orderId")
    List<OrderItem> findAllByOrderId(Long orderId);
}
