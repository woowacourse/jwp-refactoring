package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.INVALID_TABLE_UNGROUP_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.TableGroupResponse;
import kitchenpos.ui.dto.request.OrderTableIdRequest;
import kitchenpos.ui.dto.request.TableGroupRequest;
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

    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = getSavedOrderTables(request.getOrderTables());
        final TableGroup saveTableGroup = tableGroupDao.save(TableGroup.of(LocalDateTime.now(), savedOrderTables));
        return TableGroupResponse.from(saveTableGroup);
    }

    private void validateSize(final List<OrderTableIdRequest> targetOrderTables,
                              final List<OrderTable> savedOrderTables) {
        if (targetOrderTables.size() != savedOrderTables.size()) {
            throw new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION);
        }
    }

    private List<OrderTable> getSavedOrderTables(final List<OrderTableIdRequest> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateSize(orderTables, savedOrderTables);
        return savedOrderTables;
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
