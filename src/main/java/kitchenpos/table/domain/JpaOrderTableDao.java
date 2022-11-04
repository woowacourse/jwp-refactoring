package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderTableDao extends JpaRepository<OrderTable, Long>, OrderTableDao {
}
