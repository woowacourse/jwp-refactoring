package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> ids, List<String> orderStatuses);

    boolean existsByOrderTableIdAndOrderStatusIn(Long id, List<String> orderStatuses);

    List<Order> findAllByOrderTableId(Long orderTableId);

    @Query("select order from orders order where order.orderTableId in :orderTableIds")
    List<Order> findAllByOrderTableIds(List<Long> orderTableIds);
}
