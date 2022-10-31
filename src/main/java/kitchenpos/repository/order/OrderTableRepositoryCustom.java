package kitchenpos.repository.order;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.order.OrderTable;

public interface OrderTableRepositoryCustom {

    List<OrderTable> findWithTableGroupByIdIn(List<Long> ids);

    Optional<OrderTable> findWithOrdersAndTableGroupById(Long id);
}
