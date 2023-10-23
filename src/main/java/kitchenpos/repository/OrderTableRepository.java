package kitchenpos.repository;

import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

}
