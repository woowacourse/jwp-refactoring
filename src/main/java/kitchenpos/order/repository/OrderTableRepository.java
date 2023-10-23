package kitchenpos.order.repository;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    default OrderTable getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문 테이블이 존재하지 않습니다. id : " + id));
    }
}
