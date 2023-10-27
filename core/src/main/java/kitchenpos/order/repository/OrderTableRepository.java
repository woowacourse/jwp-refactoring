package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.exception.OrderTableException.NotExistsOrderTableException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(final Long orderTableId) {
        return findById(orderTableId)
                .orElseThrow(NotExistsOrderTableException::new);
    }

    List<OrderTable> findByTableGroupId(final Long tableGroupId);
}
