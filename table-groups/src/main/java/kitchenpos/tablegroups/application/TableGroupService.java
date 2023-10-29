package kitchenpos.tablegroups.application;


import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.exception.OrderTableNotFoundException;
import kitchenpos.tablegroups.domain.TableGroup;
import kitchenpos.tablegroups.exception.TableGroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.exception.OrderIsNotCompletedBadRequestException;
import kitchenpos.tablegroups.domain.TableGroupRepository;
import kitchenpos.tablegroups.dto.CreateTableGroupRequest;
import kitchenpos.tablegroups.dto.TableGroupResponse;
import kitchenpos.tablegroups.dto.UnGroupRequest;

import java.util.List;
import java.util.stream.Collectors;

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
    public TableGroupResponse create(final CreateTableGroupRequest request) {
        final List<OrderTable> orderTables = convertToOrderTables(request);
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of(orderTables));
        orderTables.forEach(orderTable -> orderTable.setTableGroupId(savedTableGroup.getId()));
        return TableGroupResponse.from(savedTableGroup);
    }

    private List<OrderTable> convertToOrderTables(final CreateTableGroupRequest request) {
        return request.getOrderTables().stream()
                .map(this::checkOrderTableExists)
                .collect(Collectors.toList());
    }

    private OrderTable checkOrderTableExists(final OrderTableRequest orderTableRequest) {
        return orderTableRepository.findById(orderTableRequest.getId())
                .orElseThrow(() -> new OrderTableNotFoundException(orderTableRequest.getId()));
    }

    @Transactional
    public void ungroup(final UnGroupRequest request) {
        final TableGroup tableGroup = convertToTableGroup(request.getTableGroupId());
        final List<OrderTable> orderTables = convertToOrderTables(request.getTableGroupId());
        orderTables.forEach(this::checkTableHasOrderAndCompleted);
        tableGroup.ungroup();
    }

    private void checkTableHasOrderAndCompleted(final OrderTable orderTable) {
        if (orderRepository.existsByOrderTableId(orderTable.getId())) {
            final Order order = convertToOrder(orderTable);
            checkOrderCompleted(orderTable, order);
        }
    }

    private Order convertToOrder(final OrderTable orderTable) {
        return orderRepository.findByOrderTableId(orderTable.getId())
                .orElseThrow(OrderNotFoundException::new);
    }

    private void checkOrderCompleted(final OrderTable orderTable, final Order order) {
        if (order.isNotCompleted()) {
            throw new OrderIsNotCompletedBadRequestException(orderTable.getId());
        }
    }

    private TableGroup convertToTableGroup(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableGroupNotFoundException(tableGroupId));
    }

    private List<OrderTable> convertToOrderTables(final Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}
