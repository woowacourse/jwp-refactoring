package kitchenpos.repository;

import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.data.repository.Repository;

public interface JpaOrderTableRepository extends Repository<OrderTable, Long>, OrderTableRepository {
}
