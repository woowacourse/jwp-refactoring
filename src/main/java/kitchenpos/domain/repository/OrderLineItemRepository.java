package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    @Query("select o from OrderLineItem o where o.order.id = :orderId")
    List<OrderLineItem> findAllByOrderId(Long orderId);
}
