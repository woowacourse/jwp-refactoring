package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> list);
}
