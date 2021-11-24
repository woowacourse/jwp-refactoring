package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final OrderTables orderTables = new OrderTables(tableGroup.getOrderTables());
        orderTables.validateGroupingNumbers();
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTables.getOrderTableIds()));

        if (orderTables.isDifferentSize(savedOrderTables)) {
            throw new IllegalArgumentException();
        }

        savedOrderTables.validateGroupingTables();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        savedOrderTables.groupingTables(savedTableGroup.getId());
        savedTableGroup.grouping(savedOrderTables.getValues());

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        List<Long> orderTableIds = orderTables.getOrderTableIds();

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        orderTables.ungroupTables();
    }
}
