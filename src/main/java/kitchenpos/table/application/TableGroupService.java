package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.ui.dto.TableGroupCreateRequest;
import kitchenpos.table.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        request.verify();
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(request.getOrderTableIds());
        if (request.getOrderTableIds().size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        final TableGroup tableGroup = tableGroupRepository.save(TableGroup.from(LocalDateTime.now()))
                .addOrderTables(orderTables);
        orderTableRepository.saveAll(tableGroup.getOrderTables());

        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        if (containNotCompletedOrder(orderTables)) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> ungroupedOrderTables = orderTables.stream()
                .map(OrderTable::ungroup)
                .collect(Collectors.toList());
        orderTableRepository.saveAll(ungroupedOrderTables);
    }

    private boolean containNotCompletedOrder(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }
}
