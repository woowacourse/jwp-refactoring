package kitchenpos.domain.repository;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    @Query("SELECT ol FROM OrderLineItem ol WHERE ol.order.id = :id")
    List<OrderLineItem> findAllBy(@Param("id") Long id);
}
