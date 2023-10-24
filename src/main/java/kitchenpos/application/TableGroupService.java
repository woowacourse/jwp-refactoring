package kitchenpos.application;

import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.application.dto.response.CreateTableGroupResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<OrderTable> orderTableEntities = getOrderTables(request.getOrderTableIds());
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

    private List<OrderTable> getOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (Long orderTableId : orderTableIds) {
            OrderTable entity = orderTableRepository.findById(orderTableId)
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
        Order order = orderRepository.findByOrderTableId(orderTable.getId());
        if (order.getOrderStatus() == OrderStatus.COOKING || order.getOrderStatus() == OrderStatus.MEAL) {
            throw new IllegalArgumentException();
        }
    }
}
