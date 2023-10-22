package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    default OrderLineItem getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderLineItemException("해당하는 주문 항목이 없습니다."));
    }
}
