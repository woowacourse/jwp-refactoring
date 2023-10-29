package tablegroups.application;


import order.domain.Order;
import order.domain.OrderRepository;
import order.exception.OrderNotFoundException;
import order.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import table.domain.OrderTable;
import table.domain.OrderTableRepository;
import table.dto.OrderTableRequest;
import table.exception.OrderIsNotCompletedBadRequestException;
import tablegroups.domain.TableGroup;
import tablegroups.domain.TableGroupRepository;
import tablegroups.dto.CreateTableGroupRequest;
import tablegroups.dto.TableGroupResponse;
import tablegroups.dto.UnGroupRequest;
import tablegroups.exception.TableGroupNotFoundException;

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
