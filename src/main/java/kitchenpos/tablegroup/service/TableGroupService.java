package kitchenpos.tablegroup.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.OrderTableNotFoundException;
import kitchenpos.ordertable.repository.TableRepository;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.NotCompleteTableUngroupException;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(TableRepository tableRepository, TableGroupRepository tableGroupRepository,
                             OrderRepository orderRepository) {
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest tableGroupCreateRequest) {
        List<Long> orderTableIds = tableGroupCreateRequest.getOrderTables();
        List<OrderTable> savedOrderOrderTables = tableRepository.findAllByIdIn(orderTableIds);
        validateNotFoundOrderTable(savedOrderOrderTables.size(), orderTableIds.size());
        OrderTables orderTables = OrderTables.forGrouping(savedOrderOrderTables);

        TableGroup tableGroup = new TableGroup();
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        orderTables.group(savedTableGroup.getId());
        return new TableGroupResponse(savedTableGroup, orderTables.getValues());
    }

    private void validateNotFoundOrderTable(int requestedOrderTableSize, int savedOrderTableSize) {
        if (requestedOrderTableSize != savedOrderTableSize) {
            throw new OrderTableNotFoundException();
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTablesInTableGroup = tableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderStatusInTable(orderTablesInTableGroup);
        OrderTables orderTables = OrderTables.forUnGrouping(orderTablesInTableGroup);
        orderTables.ungroup();
        orderTables.setEmpty();
    }

    private void validateOrderStatusInTable(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toUnmodifiableList());
        List<Order> orders = orderRepository.findAllByIdIn(orderTableIds);
        boolean notCompletion = orders.stream()
                .anyMatch(Order::isNotCompletionOrderStatus);
        if (notCompletion) {
            throw new NotCompleteTableUngroupException();
        }
    }
}
