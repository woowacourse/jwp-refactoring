package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.dto.tablegroup.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = findOrderTables(request);
        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(orderTables));
        List<OrderTable> savedOrderTables = saveOrderTables(savedTableGroup.getId(), orderTables);

        return TableGroupResponse.of(savedTableGroup,  savedOrderTables);
    }

    private List<OrderTable> findOrderTables(TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
        return orderTables;
    }

    private List<OrderTable> saveOrderTables(Long tableGroupId, List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(orderTable -> orderTableDao.save(
                        new OrderTable(orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests(), false)))
                .collect(Collectors.toList());
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTableDao.save(new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), false));
        }
    }
}
