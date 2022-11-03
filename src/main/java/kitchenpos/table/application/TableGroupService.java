package kitchenpos.table.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.dto.request.TableGroupCreateRequest;
import kitchenpos.table.application.dto.response.TableGroupResponse;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderDao orderDao, OrderTableDao orderTableDao, TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final OrderTables orderTables = new OrderTables(request.getOrderTables());
        final TableGroup savedTableGroup = tableGroupRepository.save(request.toEntity());
        final OrderTables savedOrderTables = OrderTables.from(orderTableDao.findAllByIdIn(orderTables.getIds()), orderTables);

        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(),
                savedOrderTables.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = getOrderTables(tableGroupId);

        for (final OrderTable orderTable : orderTables) {
            orderTableDao.save(new OrderTable(orderTable.getId(), orderTable.getNumberOfGuests(), false));
        }
    }

    private List<OrderTable> getOrderTables(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        validateOrderStatus(orderTableIds);
        return orderTables;
    }

    private void validateOrderStatus(final List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
