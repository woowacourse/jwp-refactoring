package kitchenpos.repositroy;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table_group.TableGroup;
import kitchenpos.support.AggregateReference;
import kitchenpos.exception.TableException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(final Long id) {
        return findById(id).orElseThrow(() -> new TableException.NotFoundException(id));
    }

    List<OrderTable> findByTableGroupId(final AggregateReference<TableGroup> tableGroupId);

    Long countByIdIn(final List<Long> id);
}
