package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(tableGroupRequest.getOrderTables());
        validateOrderTables(orderTables);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.setEmpty(false);
        }

        return TableGroupResponse.of(savedTableGroup);
    }

    private static void validateOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.hasTableGroup()) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateOrderStatusCompletion(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.removeTableGroup();
            orderTable.setEmpty(false);
        }
    }

    private void validateOrderStatusCompletion(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(COOKING, MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
