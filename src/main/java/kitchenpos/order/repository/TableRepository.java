package kitchenpos.order.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderTable;
import org.apache.juli.logging.Log;
import org.springframework.data.repository.Repository;

public interface TableRepository extends Repository<OrderTable, Log> {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    void flush();

//    List<Table> findAllByIdIn(List<Long> ids);

//    List<Table> findAllByTableGroupId(Long tableGroupId);
}
