package kitchenpos.repository;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exception.OrderTableException.NotExistsOrderTableException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(final Long orderTableId) {
        return findById(orderTableId)
                .orElseThrow(NotExistsOrderTableException::new);
    }
}
