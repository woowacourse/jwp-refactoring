package kitchenpos.application;

import kitchenpos.application.dto.convertor.TableGroupConvertor;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final TableGroup tableGroup = TableGroupConvertor.toTableGroup(request);
        final TableGroup savedTableGroup = saveTableGroup(tableGroup);
        return TableGroupConvertor.toOrderTableResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = getOrderTableIds(orderTables);

        validateOrderStatusIsCompletion(orderTableIds);
        ungroupOrderTables(orderTables);
    }

    private TableGroup saveTableGroup(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        final List<OrderTable> savedOrderTables = findAllOrderTablesByIdIn(orderTables);
        validateOrderTableIsEmptyAndNotGrouped(savedOrderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        saveOrderTables(savedOrderTables, savedTableGroup);
        return savedTableGroup;
    }

    private List<OrderTable> findAllOrderTablesByIdIn(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = getOrderTableIds(orderTables);
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블 정보가 포함되어 있습니다.");
        }
        return savedOrderTables;
    }

    private List<Long> getOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    private void validateOrderTableIsEmptyAndNotGrouped(final List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (savedOrderTable.isNotEmpty()) {
                throw new IllegalArgumentException("테이블이 비워져 있어야 합니다.");
            }
            if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException(String.format("테이블 그룹의 아이디가 존재합니다. [%s]", savedOrderTable.getTableGroupId()));
            }
        }
    }

    private void saveOrderTables(final List<OrderTable> savedOrderTables, final TableGroup savedTableGroup) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.group(savedTableGroup.getId());
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);
    }

    private void validateOrderStatusIsCompletion(final List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("테이블의 주문이 완료되지 않았습니다.");
        }
    }

    private void ungroupOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(null);
            orderTableDao.save(orderTable);
        }
    }
}
