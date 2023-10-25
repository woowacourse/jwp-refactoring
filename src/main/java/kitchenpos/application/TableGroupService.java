package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.application.dto.ordertable.OrderTableIdDto;
import kitchenpos.application.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.application.dto.tablegroup.TableGroupUngroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private static final List<String> UNGROUPABLE_ORDER_STATUSES = List.of(
            OrderStatus.COOKING.name(),
            OrderStatus.MEAL.name()
    );

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
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = getOrderTablesWithIds(request.getOrderTables());
        validateOrderTablesAllExist(request, orderTables);
        validateAllOrderTablesCanBeGrouped(orderTables);
        final TableGroup savedTableGroup = tableGroupDao.save(createTableGroupCreatedAtNow());
        savedTableGroup.addOrderTables(orderTables);
        saveAllOrderTables(orderTables);
        return savedTableGroup;
    }

    private List<OrderTable> getOrderTablesWithIds(final List<OrderTableIdDto> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableIdDto::getId)
                .collect(Collectors.toList());

        return orderTableDao.findAllByIdIn(orderTableIds);
    }

    private void validateOrderTablesAllExist(final TableGroupCreateRequest request,
                                             final List<OrderTable> savedOrderTables) {
        if (request.getOrderTables().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateAllOrderTablesCanBeGrouped(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateOrderTableCanBeGrouped(orderTable);
        }
    }

    private void validateOrderTableCanBeGrouped(final OrderTable orderTable) {
        if (!orderTable.hasNoGroupAndEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private TableGroup createTableGroupCreatedAtNow() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    private void saveAllOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTableDao.save(orderTable);
        }
    }

    @Transactional
    public void ungroup(final TableGroupUngroupRequest request) {
        final List<OrderTable> orderTables = findAllOrderTablesInGroup(request);
        validateAllOrderInTableCompleted(orderTables);
        upGroupAllOrderTables(orderTables);
    }

    private List<OrderTable> findAllOrderTablesInGroup(final TableGroupUngroupRequest request) {
        return orderTableDao.findAllByTableGroupId(request.getId());
    }

    private void validateAllOrderInTableCompleted(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, UNGROUPABLE_ORDER_STATUSES)) {
            throw new IllegalArgumentException();
        }
    }

    private void upGroupAllOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
            orderTableDao.save(orderTable);
        }
    }
}
