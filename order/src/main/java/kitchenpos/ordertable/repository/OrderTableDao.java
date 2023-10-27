package kitchenpos.ordertable.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.repository.Repository;

public interface OrderTableDao extends Repository<OrderTable, Long> {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);
}
