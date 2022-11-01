package kitchenpos.repository;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface OrderTableRepository extends JpaRepository<OrderTable, Long>, OrderTableDao {
}
