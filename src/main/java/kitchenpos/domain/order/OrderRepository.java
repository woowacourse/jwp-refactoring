package kitchenpos.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderTableId(Long orderTableId);

    List<Order> findByOrderTableIdIn(List<Long> orderTables);
}
