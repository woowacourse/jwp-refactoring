package kitchenpos;

import kitchenpos.order.domain.repository.OrderTableDao;
import org.springframework.stereotype.Repository;

@Repository
public interface TableDao extends OrderTableDao {

}
