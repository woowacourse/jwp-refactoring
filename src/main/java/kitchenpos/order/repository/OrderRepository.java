package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderTable_TableGroup_Id(Long tableGroupId);

    default Order getById(final Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다. id : " + id));
    }
}
