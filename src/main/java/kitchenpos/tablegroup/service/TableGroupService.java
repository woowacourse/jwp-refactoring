package kitchenpos.tablegroup.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.request.CreateTableGroupRequest;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.tablegroup.dto.response.TableGroupResponse;
import kitchenpos.tablegroup.exception.OrderTableCountNotEnoughException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.tablegroup.exception.TableGroupNotFoundException;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(CreateTableGroupRequest request) {
        List<OrderTable> orderTables = findOrderTables(request.getOrderTables());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        tableGroupRepository.save(tableGroup);
        groupOrderTables(tableGroup, orderTables);

        return TableGroupResponse.from(tableGroup, orderTables);
    }

    private List<OrderTable> findOrderTables(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(each -> orderTableRepository.findById(each.getId())
                        .orElseThrow(OrderTableNotFoundException::new))
                .collect(Collectors.toList());
    }

    public void groupOrderTables(TableGroup tableGroup, List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateIsEmpty();
            orderTable.validateTableGroupNotExists();

            orderTable.changeEmpty(false);
            orderTable.changeTableGroup(tableGroup.getId());
        }
        validateOrderTableCount(orderTables);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        validateAllOrderCompleted(orderTables);
        unGroupOrderTables(orderTables);
    }

    private void validateAllOrderCompleted(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        List<Order> orders = orderRepository.findByOrderTableIdIn(orderTableIds);

        for (Order order : orders) {
            order.validateOrderIsCompleted();
        }
    }

    private void validateOrderTableCount(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new OrderTableCountNotEnoughException();
        }
    }

    public void unGroupOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
        }
    }

}