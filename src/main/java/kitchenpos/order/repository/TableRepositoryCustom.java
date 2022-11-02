package kitchenpos.order.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderTable;

public interface TableRepositoryCustom {

    List<OrderTable> findAllWithTableGroup(Long tableGroupId);

    List<OrderTable> findAllWithTableGroupByIdIn(List<Long> ids);

    Optional<OrderTable> findWithTableGroupById(Long id);
}
