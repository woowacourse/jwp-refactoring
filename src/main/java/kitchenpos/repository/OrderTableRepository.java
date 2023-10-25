package kitchenpos.repository;

import kitchenpos.domain.OrderTable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable findOrderTableById(final Long orderTableId) {
        return findById(orderTableId).orElseThrow(() -> new EmptyResultDataAccessException("주문 테이블 식별자값을 주문 테이블을 조회할 수 없습니다.", 1));
    }

    List<OrderTable> findAllByIdIn(final List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);
}
