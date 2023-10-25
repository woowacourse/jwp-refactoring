package kitchenpos.table.domain;

import java.util.List;
import kitchenpos.table.exception.OrderTableException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderTableException("해당하는 주문 테이블이 없습니다."));
    }

    default List<OrderTable> getAllByIdIn(List<Long> ids) {
        List<OrderTable> orderTables = findAllByIdIn(ids);
        if (orderTables.size() != ids.size()) {
            throw new OrderTableException("존재하지 않는 주문 테이블의 ID가 포함되어 있습니다.");
        }
        return orderTables;
    }

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
