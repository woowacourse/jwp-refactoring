package kitchenpos.domain.order;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);
}
