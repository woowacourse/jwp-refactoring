package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.dto.request.TableIdRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public TableGroupResponse create(TableGroupCreateRequest request) {
        List<TableIdRequest> orderTablesRequest = request.getOrderTables();
        
        validateOrderTablesSize(orderTablesRequest);
        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(toTableRequestIds(orderTablesRequest));
        validateRequestTablesExistence(orderTablesRequest, savedOrderTables);

        TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), savedOrderTables));

        Long tableGroupId = savedTableGroup.getId(); // TODO: 이걸 Repository에서
        List<OrderTable> updatedOrderTables = savedOrderTables.stream()
                .map(orderTable -> orderTableDao.save(
                        new OrderTable(
                                orderTable.getId(),
                                tableGroupId,
                                orderTable.getNumberOfGuests(),
                                false
                        )))
                .collect(Collectors.toList());

        return TableGroupResponse.of(savedTableGroup, updatedOrderTables);
    }

    private void validateOrderTablesSize(List<TableIdRequest> orderTablesRequest) {
        if (CollectionUtils.isEmpty(orderTablesRequest) || orderTablesRequest.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> toTableRequestIds(List<TableIdRequest> orderTablesRequest) {
        return orderTablesRequest.stream()
                .map(TableIdRequest::getId)
                .collect(Collectors.toList());
    }

    private void validateRequestTablesExistence(List<TableIdRequest> orderTablesRequest, List<OrderTable> savedOrderTables) {
        if (orderTablesRequest.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateOrderStatusCompletion(orderTables);

        updateEachTable(orderTables);
    }

    private void validateOrderStatusCompletion(List<OrderTable> orderTables) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                toTableIds(orderTables), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> toTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void updateEachTable(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            OrderTable newOrderTable = new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), false);
            orderTableDao.save(newOrderTable);
        }
    }
}
