package kitchenpos.order_table.repository;

import java.util.List;
import kitchenpos.order_table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
