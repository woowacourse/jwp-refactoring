package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.data.repository.Repository;

public interface OrderTableDao extends Repository<OrderTable, Long> {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
