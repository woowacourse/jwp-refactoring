package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.TableGroupOrderTableRequest;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.common.exception.InvalidOrderException;
import kitchenpos.common.exception.InvalidTableException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.OrderTableResponse;
import kitchenpos.ui.dto.TableGroupResponse;
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

    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = getOrderTableIds(tableGroupRequest);
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateTables(orderTableIds, orderTables);

        TableGroup tableGroup = tableGroupDao.save(TableGroup.create(LocalDateTime.now(), orderTables));
        updateOrderTableEmpty(orderTables);

        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                mapToOrderTableResponses(orderTables));
    }

    private void updateOrderTableEmpty(List<OrderTable> orderTables) {
        orderTables.stream()
                .map(orderTable -> new OrderTable(orderTable.getId(), orderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(), false))
                .forEach(orderTableDao::save);
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

    private List<OrderTableResponse> mapToOrderTableResponses(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(orderTable -> new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());
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
