package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderTable_TableGroup_Id(Long tableGroupId);

    List<Order> findAllByOrderTableId(Long orderTableId);
}
