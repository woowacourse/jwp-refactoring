package kitchenpos.repository;

import kitchenpos.domain.order.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

}
