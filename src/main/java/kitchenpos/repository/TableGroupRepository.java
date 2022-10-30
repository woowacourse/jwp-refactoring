package kitchenpos.repository;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TableGroupRepository {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupRepository(final OrderTableDao orderTableDao,
                                final TableGroupDao tableGroupDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup save(final TableGroup entity) {
        List<Long> orderTableIds = toOrderTableIds(entity);

        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateSizeOrderTable(orderTableIds, savedOrderTables);
        validateEmptyOrderTables(savedOrderTables);

        return tableGroupDao.save(entity);
    }

    private List<Long> toOrderTableIds(final TableGroup entity) {
        return entity.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(toList());
    }

    private void validateSizeOrderTable(final List<Long> orderTableIds, final List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmptyOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }
}
