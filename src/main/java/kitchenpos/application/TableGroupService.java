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
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 포함되어 있습니다.");
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
        validateOrderTablesForUnGroup(orderTables);
        orderTables.forEach(this::unbindTableFromGroup);
    }

    private void validateOrderTablesForUnGroup(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중 or 식사중인 주문이 포함되어 있습니다.");
        }
    }

    private void unbindTableFromGroup(final OrderTable orderTable) {
        orderTable.setTableGroupId(null);
        orderTable.changeEmpty(false);
        orderTableDao.save(orderTable);
    }
}
