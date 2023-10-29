package kitchenpos.table.domain;

import static kitchenpos.table.exception.OrderTableExceptionType.ORDER_TABLE_NOT_FOUND;

import java.util.List;
import kitchenpos.table.exception.OrderTableException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Override
    default OrderTable getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderTableException(ORDER_TABLE_NOT_FOUND));
    }

    default List<OrderTable> findAllByIdInOrElseThrow(List<Long> ids) {
        List<OrderTable> orderTables = findAllByIdIn(ids);
        if (ids.size() != orderTables.size()) {
            throw new OrderTableException(ORDER_TABLE_NOT_FOUND);
        }
        return orderTables;
    }

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
