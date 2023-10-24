package kitchenpos.domain.order.service;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTables;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.repository.OrderTableRepository;
import kitchenpos.domain.order.repository.TableGroupRepository;
import kitchenpos.domain.order.service.dto.TableGroupCreateRequest;
import kitchenpos.domain.order.service.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
@Transactional(readOnly = true)
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
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTables();
        final OrderTables orderTables = OrderTables.from(orderTableRepository.findAllByIdIn(orderTableIds));

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(now(), new OrderTables()));
        savedTableGroup.addOrderTables(orderTables.getOrderTables());

        return TableGroupResponse.toDto(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        orderTables.forEach(orderTable -> {
            orderTable.changeGroup(null);
            orderTable.changeEmpty(false);
        });
    }
}
