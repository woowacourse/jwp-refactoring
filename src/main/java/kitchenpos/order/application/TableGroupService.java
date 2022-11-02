package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.response.TableGroupResponse;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.menu.application.request.TableGroupRequest;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.dao.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.application.request.OrderTableRequest;
import kitchenpos.order.domain.TableGroupValidator;

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
    public TableGroupResponse create(final TableGroupRequest request) {
        List<Long> orderTableIds = getOrderTableIds(request);
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        TableGroupValidator validator = new TableGroupValidator();
        validator.validateOrderTables(orderTables);

        TableGroup tableGroup = tableGroupDao.save(new TableGroup());
        tableGroup.groupOrderTables(orderTables);
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        List<Long> orderTableIds = getOrderTableIds(orderTables);
        validateOrdersNotCompletion(orderTableIds);

        TableGroup tableGroup = tableGroupDao.getById(tableGroupId);
        tableGroup.ungroupOrderTables(orderTables);
    }

    private List<Long> getOrderTableIds(final TableGroupRequest request) {
        return request.getOrderTables().stream()
            .map(OrderTableRequest::getId)
            .collect(Collectors.toUnmodifiableList());
    }

    public void validateOrdersNotCompletion(final List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> getOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toUnmodifiableList());
    }
}
