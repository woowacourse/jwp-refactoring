package kitchenpos.repository;

import static kitchenpos.exception.OrderTableExceptionType.NOT_EXIST_ORDER_TABLE;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.OrderTableException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(Long id) {
        return findById(id).orElseThrow(() -> new OrderTableException(NOT_EXIST_ORDER_TABLE));
    }

    List<OrderTable> getByIdIn(List<Long> ids);
}
