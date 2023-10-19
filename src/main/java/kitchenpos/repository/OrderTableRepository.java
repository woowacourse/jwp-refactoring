package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exception.OrderTableException.NotExistsOrderTableException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    long countByIdIn(final List<Long> ids);

    default OrderTable getById(final Long orderTableId) {
        return findById(orderTableId)
                .orElseThrow(NotExistsOrderTableException::new);
    }
}
