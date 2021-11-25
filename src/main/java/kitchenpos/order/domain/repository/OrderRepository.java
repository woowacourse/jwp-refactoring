package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // TODO: 추후 제거 대상인지 확인하기
    List<Order> findAllByOrderTableId(Long orderTableId);
}
