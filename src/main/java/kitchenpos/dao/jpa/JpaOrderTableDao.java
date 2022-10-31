package kitchenpos.dao.jpa;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderTableDao extends OrderTableDao, JpaRepository<OrderTable, Long> {
}
