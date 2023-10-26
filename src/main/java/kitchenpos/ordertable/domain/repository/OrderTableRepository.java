package kitchenpos.ordertable.domain.repository;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findByTableGroup(final TableGroup tableGroup);
}
