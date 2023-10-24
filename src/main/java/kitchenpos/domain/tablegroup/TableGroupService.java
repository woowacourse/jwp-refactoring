package kitchenpos.domain.tablegroup;

import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.application.dto.response.CreateTableGroupResponse;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.application.dto.request.CreateTableGroupRequest.CreateOrderTable;

@Service
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
    public CreateTableGroupResponse create(final CreateTableGroupRequest request) {
        final List<OrderTable> orderTableEntities = getOrderTables(request.getOrderTables());
        TableGroup tableGroup = TableGroup.builder()
                .createdDate(LocalDateTime.now())
                .orderTables(orderTableEntities)
                .build();
        final TableGroup entity = tableGroupRepository.save(tableGroup);

        List<OrderTable> orderTables = getFilledOrderTables(orderTableEntities);
        TableGroup updated = entity.updateOrderTables(orderTables);
        return CreateTableGroupResponse.from(updated);
    }

    private List<OrderTable> getFilledOrderTables(List<OrderTable> orderTableEntities) {
        return orderTableEntities.stream()
                .map(OrderTable::fillTable)
                .map(orderTableRepository::save)
                .collect(Collectors.toList());
    }

    private List<OrderTable> getOrderTables(List<CreateOrderTable> orderTableIds) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (CreateOrderTable createOrderTable : orderTableIds) {
            OrderTable entity = orderTableRepository.findById(createOrderTable.getId())
                    .orElseThrow(IllegalArgumentException::new);
            orderTables.add(entity);
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        for (final OrderTable orderTable : tableGroup.getOrderTables()) {
            validateOrderTable(orderTable);
            OrderTable updated = orderTable.ungroup();
            orderTableRepository.save(updated);
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        Order order = orderRepository.findByOrderTableId(orderTable.getId())
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        if (order.getOrderStatus() == OrderStatus.COOKING || order.getOrderStatus() == OrderStatus.MEAL) {
            throw new IllegalArgumentException();
        }
    }
}
