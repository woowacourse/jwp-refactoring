package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTables;
import kitchenpos.domain.tableGroup.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.OrderTableId;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTables()
                .stream()
                .map(OrderTableId::getId)
                .collect(Collectors.toList());
        OrderTables orderTables = OrderTables.from(orderTableRepository.findAllByIdIn(orderTableIds),orderTableIds.size());
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        for (final OrderTable orderTable : orderTables.getOrderTables()) {
            orderTable.updateEmpty(false);
            orderTable.updateTableGroupId(tableGroup.getId());
        }
        return TableGroupResponse.toResponse(tableGroupRepository.save(tableGroup));
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
        for (final OrderTable orderTable : orderTables) {
            removeInTableGroup(orderTable);
        }
    }

    private void removeInTableGroup(OrderTable orderTable) {
        orderTable.removeTableGroup();
        orderTable.updateEmpty(false);
    }
}
