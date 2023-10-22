package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(
            final OrderTableDao orderTableDao,
            final TableGroupDao tableGroupDao
    ) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final List<Long> tableIds) {

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(tableIds);

        if (tableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        OrderTables orderTables = new OrderTables(savedOrderTables);
        TableGroup tableGroup = orderTables.group();

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("테이블 그룹이 존재하지 않습니다."));

        List<OrderTable> orderTables = tableGroup.getOrderTables();

        OrderTables groupedOrderTables = new OrderTables(orderTables);
        groupedOrderTables.unGroup();

        for (final OrderTable orderTable : orderTables) {
            orderTableDao.save(orderTable);
        }
    }
}
