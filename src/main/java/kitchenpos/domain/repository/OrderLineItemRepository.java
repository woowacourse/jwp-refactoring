package kitchenpos.domain.repository;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    default OrderLineItem getById(final Long id) {
        return findById(id).orElseThrow(() -> new IllegalStateException("해당하는 주문 항목이 없습니다."));
    }
}
