package kitchenpos.domain.repository;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findAllByOrderId(Long id);

    default OrderLineItem getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalStateException("해당하는 주문 항목이 없습니다."));
    }
}
