package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.application.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.application.response.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {

    private OrderRepository orderRepository;
    private OrderTableRepository orderTableRepository;
    private TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        tableGroupCreateRequest.validate();

        List<Long> orderTableIds = tableGroupCreateRequest.getOrderTableIds();
        OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));

        TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), savedOrderTables);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        savedOrderTables.setTableGroup(savedTableGroup.getId());

        return TableGroupResponse.from(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        checkIfAnyOrderIsNotCompleted(orderTableIds);

        orderTables.forEach(OrderTable::unGroup);
    }

    private void checkIfAnyOrderIsNotCompleted(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
