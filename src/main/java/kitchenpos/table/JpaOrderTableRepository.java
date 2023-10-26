package kitchenpos.table;

import java.util.List;
import kitchenpos.table.OrderTable;
import kitchenpos.table.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroup(TableGroup tableGroup);

}
