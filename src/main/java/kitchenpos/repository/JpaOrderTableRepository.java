package kitchenpos.repository;

import kitchenpos.domain.order.OrderTable;
import org.springframework.data.repository.Repository;

public interface JpaOrderTableRepository extends Repository<OrderTable, Long>, OrderTableRepository {
}
