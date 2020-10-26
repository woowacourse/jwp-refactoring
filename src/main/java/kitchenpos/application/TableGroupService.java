package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableDto> orderTableDtos = tableGroupRequest.getOrderTables();
        List<OrderTable> savedOrderTables = orderTableDtos.stream()
                .map(orderTableDto -> orderTableDao.findById(orderTableDto.getId()).orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());

        TableGroup TableGroupToSave = TableGroup.of(savedOrderTables);
        final TableGroup savedTableGroup = tableGroupDao.save(TableGroupToSave);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.tableGrouping(savedTableGroup);
        }

        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        for (final OrderTable orderTable : orderTables) {
            List<Order> ordersByOrderTable = orderDao.findAllByOrderTable(orderTable);
            orderTable.tableUngrouping(ordersByOrderTable);
            orderTableDao.save(orderTable);
        }
    }
}
