package kitchenpos.dao.jpa;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JpaOrderTableDao extends OrderTableDao, JpaRepository<OrderTable, Long> {
}
