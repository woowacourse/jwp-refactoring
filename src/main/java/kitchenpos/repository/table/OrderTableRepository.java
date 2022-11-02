package kitchenpos.repository.table;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.table.OrderTable;
import org.apache.juli.logging.Log;
import org.springframework.data.repository.Repository;

public interface OrderTableRepository extends Repository<OrderTable, Log> {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    void flush();
}
