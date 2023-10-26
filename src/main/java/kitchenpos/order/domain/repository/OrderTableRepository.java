package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

}
