package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderTableDao extends JpaRepository<OrderTable, Long>, OrderTableDao {
}
