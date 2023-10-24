package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    default OrderLineItem getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderLineItemException("해당하는 주문 항목이 없습니다."));
    }
}
