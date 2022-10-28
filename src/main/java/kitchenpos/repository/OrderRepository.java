package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kitchenpos.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value="select o from Order o where o.orderTable.id in :orderTableIds and o.orderStatus in :status")
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> status);
}
