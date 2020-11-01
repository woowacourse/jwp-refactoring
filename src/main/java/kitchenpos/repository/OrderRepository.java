package kitchenpos.repository;

import kitchenpos.domain.order.Orderz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orderz, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> asList);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> asList);
}
