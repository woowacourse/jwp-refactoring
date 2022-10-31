package kitchenpos.repository;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long>, OrderTableRepository {
}
