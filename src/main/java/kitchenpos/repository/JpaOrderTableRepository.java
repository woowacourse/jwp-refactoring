package kitchenpos.repository;

import kitchenpos.domain.order.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long>, OrderTableRepository {
}
