package kitchenpos.ordertable.domain.repository;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(final Long orderTableId) {
        return findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
    }

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);

    List<OrderTable> findAllByIdIn(final List<Long> orderTableIds);
}
