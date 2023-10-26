package kitchenpos.order.domain.repository;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findByTableGroup(final TableGroup tableGroup);
}
