package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.tablegroup.TableGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByTable_IdAndOrderStatusIn(Long tableId, List<OrderStatus> orderStatuses);

    boolean existsByTableTableGroupAndOrderStatusIn(TableGroup tableGroup,
        List<OrderStatus> orderStatuses);

    @EntityGraph(attributePaths = {"table"})
    @Override
    List<Order> findAll();
}
