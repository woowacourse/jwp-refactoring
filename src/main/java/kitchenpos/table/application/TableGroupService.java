package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.application.dto.TableGroupResponse;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.ui.dto.OrderTableDto;
import kitchenpos.table.ui.dto.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final OrderTableRequest request) {
        validateRequestOrderTablesSize(request);

        final List<OrderTable> orderTables = findOrderTables(request);
        validateOrderTablesSize(request, orderTables);
        validateHaveNotEmptyOrderTable(orderTables);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        savedTableGroup.addOrderTables(orderTables);

        updateOrderTables(savedTableGroup);

        return TableGroupResponse.of(savedTableGroup);
    }

    private void validateRequestOrderTablesSize(final OrderTableRequest request) {
        List<OrderTableDto> orderTables = request.getOrderTables();
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> findOrderTables(final OrderTableRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTables()
                .stream()
                .map(OrderTableDto::getId)
                .collect(Collectors.toList());

        return orderTableDao.findAllByIdIn(orderTableIds);
    }

    private void validateOrderTablesSize(final OrderTableRequest request, final List<OrderTable> savedOrderTables) {
        if (request.getOrderTables().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateHaveNotEmptyOrderTable(final List<OrderTable> orderTables) {
        boolean isNotEmptyOrderTable = orderTables.stream()
                .anyMatch(OrderTable::isNotEmpty);

        if (isNotEmptyOrderTable) {
            throw new IllegalArgumentException();
        }
    }

    private void updateOrderTables(final TableGroup tableGroup) {
        final Long tableGroupId = tableGroup.getId();

        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable savedOrderTable = new OrderTable(orderTable.getId(), tableGroupId,
                    orderTable.getNumberOfGuests(), false);
            orderTableDao.save(savedOrderTable);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateCannotUngroupCondition(orderTables);

        for (final OrderTable orderTable : orderTables) {
            OrderTable updatedOrderTable = new OrderTable(orderTable.getId(), orderTable.getNumberOfGuests(), false);
            orderTableDao.save(updatedOrderTable);
        }
    }

    private void validateCannotUngroupCondition(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = extractOrderTableIds(orderTables);

        List<String> conditions = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        boolean haveConditions = orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, conditions);

        if (haveConditions) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> extractOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
