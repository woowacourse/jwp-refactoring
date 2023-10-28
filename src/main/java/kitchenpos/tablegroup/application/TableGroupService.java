package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.global.exception.KitchenposException;
import kitchenpos.order.OrderRepository;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.tablegroup.TableGroupRepository;
import kitchenpos.tablegroup.ui.dto.CreateTableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.global.exception.ExceptionInformation.*;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final CreateTableGroupRequest createTableGroupRequest) {
        final List<OrderTable> requestOrderTables = createTableGroupRequest.getOrderTables();

        final List<Long> orderTableIds = requestOrderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = findOrderTables(requestOrderTables, orderTableIds);

        final TableGroup tableGroup = TableGroup.create(savedOrderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        tableGroup.updateOrderTablesGrouped();

        return savedTableGroup;
    }

    private List<OrderTable> findOrderTables(final List<OrderTable> orderTables, final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new KitchenposException(TABLE_GROUP_UNDER_BOUNCE);
        }
        final List<OrderTable> savedOrderTables = orderTableRepository.findByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new KitchenposException(ORDER_TABLE_IN_TABLE_GROUP_NOT_FOUND_OR_DUPLICATED);
        }
        return savedOrderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new KitchenposException(TABLE_GROUP_NOT_FOUND));

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(tableGroup.getOrderTableIds(), OrderStatus.getNotCompleteStatus())) {
            throw new KitchenposException(UNGROUP_NOT_COMPLETED_ORDER_TABLE);
        }

        tableGroup.updateOrderTablesUngrouped();
    }
}
