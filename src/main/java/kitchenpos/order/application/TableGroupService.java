package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.order.repository.TableGroupDao;

@Service
public class TableGroupService {
    private final TableGroupDao tableGroupDao;
    private final TableService tableService;
    private final OrderDao orderDao;

    public TableGroupService(TableGroupDao tableGroupDao, TableService tableService, OrderDao orderDao) {
        this.tableGroupDao = tableGroupDao;
        this.tableService = tableService;
        this.orderDao = orderDao;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final List<OrderTable> savedOrderTables = tableService.findAllByIdIn(orderTableIds);
        validateGroupCreation(orderTableIds, savedOrderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroupRequest.toEntity());
        savedTableGroup.addOrderTables(savedOrderTables);
        savedOrderTables.forEach(table -> table.group(savedTableGroup));
        return savedTableGroup;
    }

    private void validateGroupCreation(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        validateTableSize(orderTableIds, savedOrderTables);
        validateTableStatus(savedOrderTables);
    }

    private void validateTableSize(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() < 2) {
            throw new IllegalArgumentException("테이블 비어있거나 2보다 작습니다.");
        }

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("저장된 테이블 수와 요청 테이블 수가 일치하지 않습니다.");
        }
    }

    private void validateTableStatus(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException("주문 테이블이 비어있지 않거나, 이미 다른 그룹에 속해있습니다.");
            }
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = tableService.findAllByTableGroupId(tableGroupId);
        validateOrderStatus(orderTables);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateOrderStatus(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                                                    .map(OrderTable::getId)
                                                    .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("식사가 끝나지 않아 해제할 수 없습니다.");
        }
    }
}
