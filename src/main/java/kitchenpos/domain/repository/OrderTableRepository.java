package kitchenpos.domain.repository;

import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(Long orderTableId) {
        return findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
    }

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);
}
