package kitchenpos.order.domain;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order findOrderById(final Long orderId) {
        return findById(orderId).orElseThrow(() -> new EmptyResultDataAccessException("주문 식별자값으로 주문을 조회할 수 없습니다.", 1));
    }

    default Order findOrderByOrderTableId(final Long orderTableId) {
        return findByOrderTableId(orderTableId).orElseThrow(() -> new EmptyResultDataAccessException("주문 테이블 식별자값으로 주문을 조회할 수 없습니다.", 1));
    }

    Optional<Order> findByOrderTableId(@Param("orderTableId") final Long orderTableId);

    @Query("select o " +
           "from Order o " +
           "where o.orderTableId in :orderTableIds")
    List<Order> findInOrderTableIds(@Param("orderTableIds") final List<Long> orderTableIds);
}
