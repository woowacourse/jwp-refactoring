package kitchenpos.table;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long>, OrderTableRepository {
}
