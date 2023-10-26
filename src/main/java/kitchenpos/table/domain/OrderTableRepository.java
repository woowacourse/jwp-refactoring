package kitchenpos.table.domain;

import static kitchenpos.table.domain.exception.OrderTableExceptionType.ORDER_TABLE_IS_NOT_FOUND;

import java.util.List;
import kitchenpos.table.domain.exception.OrderTableException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(final List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    default OrderTable getById(final Long id) {
        return findById(id).orElseThrow(() -> new OrderTableException(ORDER_TABLE_IS_NOT_FOUND));
    }
}
