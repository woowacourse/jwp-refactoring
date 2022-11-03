package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.application.dto.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(
                collectTableIds(request.getOrderTables()));
        compareSize(savedOrderTables, request.getOrderTables());

        final OrderTables orderTables = new OrderTables(savedOrderTables);
        orderTables.checkCanGroup();
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.ofNew(orderTables));

        final Long tableGroupId = savedTableGroup.getId();
        saveOrderTablesWithTableGroupId(orderTables, tableGroupId);
        savedTableGroup.setOrderTables(orderTables);

        return savedTableGroup;
    }

    private List<Long> collectTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void compareSize(List<OrderTable> savedOrderTables, List<OrderTable> orderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void saveOrderTablesWithTableGroupId(OrderTables orderTables, Long tableGroupId) {
        for (final OrderTable orderTable : orderTables.getValue()) {
            orderTable.setTableGroupId(tableGroupId);
            orderTable.setEmpty(false);
            orderTableRepository.save(orderTable);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = collectTableIds(orderTables);

        checkOrdersInProgress(orderTableIds);
        saveOrderTablesWithTableGroupId(OrderTables.of(orderTables), null);
    }

    private void checkOrdersInProgress(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.collectInProgress())) {
            throw new IllegalArgumentException();
        }
    }
}
