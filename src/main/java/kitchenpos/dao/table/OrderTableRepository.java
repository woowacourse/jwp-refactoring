package kitchenpos.dao.table;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroup(TableGroup tableGroup);
}
