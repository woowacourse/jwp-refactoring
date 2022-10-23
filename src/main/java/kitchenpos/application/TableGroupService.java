package kitchenpos.application;

import kitchenpos.application.dto.request.CreateTableGroupDto;
import kitchenpos.application.dto.response.TableGroupDto;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(OrderDao orderDao, OrderTableDao orderTableDao, TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupDto create(CreateTableGroupDto createTableGroupDto) {
        List<Long> tableIds = createTableGroupDto.getOrderTableIds();
        OrderTables orderTables = findValidOrderTables(tableIds);
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup());
        orderTables.group(tableGroup.getId());
        for (final OrderTable savedOrderTable : orderTables.getValue()) {
            orderTableDao.save(savedOrderTable);
        }
        return TableGroupDto.of(tableGroup, orderTables.getValue());
    }

    private OrderTables findValidOrderTables(List<Long> tableIds) {
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(tableIds);
        if (tableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
        return OrderTables.ofNotGroupedOrderTables(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateNoOngoingOrder(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.removeTableGroupId();
            orderTableDao.save(orderTable);
        }
    }

    private void validateNoOngoingOrder(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.getOngoingStatuses())) {
            throw new IllegalArgumentException();
        }
    }
}
