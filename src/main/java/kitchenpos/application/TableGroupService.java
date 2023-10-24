package kitchenpos.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupValidator;
import kitchenpos.dto.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao, final TableGroupValidator tableGroupValidator) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(request.getOrderTableIds());
        validateOrderTablesIsExist(request, savedOrderTables);
        final TableGroup tableGroup = new TableGroup(savedOrderTables);
        tableGroupValidator.validate(tableGroup);
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        savedTableGroup.setOrderTables(saveOrderTables(savedTableGroup.getId(), savedOrderTables));
        return savedTableGroup;
    }

    private static void validateOrderTablesIsExist(final TableGroupRequest request,
                                                   final List<OrderTable> savedOrderTables) {
        if (savedOrderTables.size() != request.getOrderTableIds().size()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> saveOrderTables(final Long tableGroupId, final List<OrderTable> orderTables) {
        final List<OrderTable> savedOrderTables = new ArrayList<>();
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(tableGroupId);
            orderTable.changeEmpty(false);
            savedOrderTables.add(orderTableDao.save(orderTable));
        }
        return savedOrderTables;
    }

    @Transactional
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
            orderTable.setTableGroupId(null);
            orderTable.changeEmpty(false);
            orderTableDao.save(orderTable);
        }
    }
}
