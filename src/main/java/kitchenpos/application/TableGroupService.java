package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
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
        if (tableGroupRequest.getOrderTables().size() < 2) {
            throw new IllegalArgumentException("등록되는 테이블 수가 2 이상이어야 한다.");
        }

        final List<OrderTable> orderTables = getOrderTables(tableGroupRequest);
        validateNoGroupedTable(orderTables);
        validateTableIsEmpty(orderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(null, LocalDateTime.now(), orderTables));

        updateToFull(savedTableGroup);
        addTableGroupId(savedTableGroup);

        return toTableGroupResponse(savedTableGroup);
    }

    private void validateTableIsEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalArgumentException("등록되는 모든 테이블들은 비어있어야 한다.");
        }
    }

    private void validateNoGroupedTable(List<OrderTable> orderTables) {
        if (orderTables.stream().anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroupId()))) {
            throw new IllegalArgumentException("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.");
        }
    }

    private List<OrderTable> getOrderTables(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables().stream()
                .map(orderTableRequest -> orderTableDao.findById(orderTableRequest.getId()))
                .collect(Collectors.toList());
    }

    private List<Long> getOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void updateToFull(TableGroup savedTableGroup) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (final OrderTable savedOrderTable : savedTableGroup.getOrderTables()) {
            savedOrderTable.updateEmpty(false);
            orderTables.add(orderTableDao.save(savedOrderTable));
        }
        savedTableGroup.updateOrderTables(orderTables);
    }

    private void addTableGroupId(TableGroup savedTableGroup) {
        final Long tableGroupId = savedTableGroup.getId();
        List<OrderTable> orderTables = new ArrayList<>();
        for (final OrderTable savedOrderTable : savedTableGroup.getOrderTables()) {
            savedOrderTable.updateTableGroupId(tableGroupId);
            orderTables.add(orderTableDao.save(savedOrderTable));
        }
        savedTableGroup.updateOrderTables(orderTables);
    }

    private TableGroupResponse toTableGroupResponse(TableGroup savedTableGroup) {
        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(),
                getOrderTableResponses(savedTableGroup.getOrderTables()));
    }

    private List<OrderTableResponse> getOrderTableResponses(List<OrderTable> savedOrderTables) {
        return savedOrderTables.stream()
                .map(this::toOrderTableResponse)
                .collect(Collectors.toList());
    }

    private OrderTableResponse toOrderTableResponse(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        validateComplete(getOrderTableIds(orderTables));

        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroupId(null);
            orderTable.updateEmpty(false);
            orderTableDao.save(orderTable);
        }
    }

    private void validateComplete(List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.");
        }
    }
}
