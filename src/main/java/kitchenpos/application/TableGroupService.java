package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableIdRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.TableGroupCreateRequestDto;
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
    public TableGroupResponse create(final TableGroupCreateRequestDto dto) {
        final List<OrderTableIdRequest> orderTables = dto.getOrderTables();
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(getRequestOrderTablesIds(orderTables));

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        tableGroup.validateIsEqualToOrderTablesSize(orderTables.size());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        mergeOrderTables(savedOrderTables, tableGroup);
        return new TableGroupResponse(
                savedTableGroup.getId(),
                savedTableGroup.getCreatedDate(),
                savedTableGroup.getOrderTables()
        );
    }

    private List<Long> getRequestOrderTablesIds(List<OrderTableIdRequest> orderTables) {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    private void mergeOrderTables(List<OrderTable> savedOrderTables, TableGroup tableGroup) {
        final Long tableGroupId = tableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.merge(tableGroupId);
            orderTableDao.save(savedOrderTable);
        }
        tableGroup.setOrderTables(savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = getOrderTablesIds(orderTables);

        validateIsPossibleOrderStatus(orderTableIds);
        splitOrderTables(orderTables);
    }

    private void validateIsPossibleOrderStatus(List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문 상태가 요리중이거나 먹는중 상태면 테이블을 분리할 수 없습니다.");
        }
    }

    private List<Long> getOrderTablesIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void splitOrderTables(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableDao.save(orderTable);
        }
    }
}
