package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(final List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);

    default OrderTable getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문 테이블이 존재하지 않습니다. id : " + id));
    }
}
