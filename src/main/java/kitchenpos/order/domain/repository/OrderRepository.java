package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.orderTable.tableGroup.id = :tableGroupId")
    List<Order> findByTableGroup(Long tableGroupId);

    @Query("select o from Order o where o.orderTable.id = :orderTableId")
    List<Order> findByOrderTable(Long orderTableId);
}
