package kitchenpos.domain;

import static kitchenpos.exception.OrderTableExceptionType.ORDER_TABLE_NOT_FOUND;

import java.util.List;
import kitchenpos.exception.OrderTableException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Override
    default OrderTable getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderTableException(ORDER_TABLE_NOT_FOUND));
    }

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
