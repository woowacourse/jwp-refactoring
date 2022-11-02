package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.InvalidOrderException;
import kitchenpos.common.exception.InvalidTableException;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.dto.request.TableGroupOrderTableRequest;
import kitchenpos.table.application.dto.request.TableGroupRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(OrderDao orderDao, OrderTableDao orderTableDao, TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    public Long create(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = getOrderTableIds(tableGroupRequest);
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateTables(orderTableIds, orderTables);

        TableGroup tableGroup = TableGroup.create(LocalDateTime.now(), orderTables);
        Long tableGroupId = tableGroupDao.save(tableGroup);
        updateOrderTableEmpty(orderTables);

        return tableGroupId;
    }

    private void updateOrderTableEmpty(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTableDao.updateEmpty(orderTable.getId(), false);
        }
    }

    private List<Long> getOrderTableIds(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables()
                .stream()
                .map(TableGroupOrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    private void validateTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new InvalidTableException("테이블이 존재하지 않습니다.");
        }
    }

    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        List<Long> orderTableIds = getOrderTableIds(orderTables);
        validateOrderTableStatus(orderTableIds);
        ungroupOrderTables(orderTables);
    }

    private List<Long> getOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void validateOrderTableStatus(List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new InvalidOrderException("주문이 완료 상태가 아닙니다.");
        }
    }

    private void ungroupOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTableDao.updateTableGroupIdAndEmpty(orderTable.getId(), null, false);
        }
    }
}
