package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableIdRequest;
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

        if (CollectionUtils.isEmpty(orderTablesRequest) || orderTablesRequest.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTablesRequest.stream()
                .map(TableIdRequest::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTablesRequest.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), savedOrderTables));

        Long tableGroupId = savedTableGroup.getId();
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

    @Transactional
    public void ungroup(Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            OrderTable newOrderTable = new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), false);
            orderTableDao.save(newOrderTable);
        }
    }
}
