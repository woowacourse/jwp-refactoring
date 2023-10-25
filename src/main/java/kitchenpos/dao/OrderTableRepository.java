package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(final List<Long> ids);

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);

    default OrderTable getById(final Long orderTableId){
        return findById(orderTableId)
                .orElseThrow(() -> new RuntimeException("주문 테이블 ID가 존재하지 않습니다."));
    }
}
