package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.TableGroup;
import kitchenpos.menu.ui.request.TableGroupRequest;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.dao.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.ui.request.OrderTableRequest;

@Service
@Transactional(readOnly = true)
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
    public TableGroup create(final TableGroupRequest request) {
        List<Long> orderTableIds = getOrderTableIds(request);
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);

        return tableGroupDao.save(new TableGroup(orderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.getById(tableGroupId);
        List<Long> orderTableIds = getOrderTableIds(tableGroup);
        validateOrdersNotCompletion(orderTableIds);

        tableGroup.ungroupOrderTables();
    }

    private List<Long> getOrderTableIds(final TableGroupRequest request) {
        return request.getOrderTables().stream()
            .map(OrderTableRequest::getId)
            .collect(Collectors.toUnmodifiableList());
    }

    private List<Long> getOrderTableIds(final TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toUnmodifiableList());
    }

    private void validateOrdersNotCompletion(final List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
