package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.INVALID_TABLE_UNGROUP_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.OrderTableIdRequest;
import kitchenpos.ui.dto.TableGroupRequest;
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

    public TableGroup create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = getRequestOrderTables(request.getOrderTables());
        validateSize(request.getOrderTables(), savedOrderTables);
        final TableGroup tableGroup = convertSavaTableGroup(request, savedOrderTables);
        return tableGroupDao.save(tableGroup);
    }

    private TableGroup convertSavaTableGroup(final TableGroupRequest request, final List<OrderTable> savedOrderTables) {
        return new TableGroup(request.getId(), request.getCreatedDate(), savedOrderTables);
    }

    private void validateSize(final List<OrderTableIdRequest> targetOrderTables,
                              final List<OrderTable> savedOrderTables) {
        if (targetOrderTables.size() != savedOrderTables.size()) {
            throw new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION);
        }
    }

    private List<OrderTable> getRequestOrderTables(final List<OrderTableIdRequest> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
        return orderTableDao.findAllByIdIn(orderTableIds);
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validChangeOrderTableStatusCondition(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.ClearTable();
            orderTableDao.save(orderTable);
        }
    }

    private void validChangeOrderTableStatusCondition(final List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(COOKING.name(), MEAL.name()))) {
            throw new CustomIllegalArgumentException(INVALID_TABLE_UNGROUP_EXCEPTION);
        }
    }
}
