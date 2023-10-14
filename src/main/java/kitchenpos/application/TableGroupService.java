package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.CreateTableGroupRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.persistence.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        List<OrderTableRequest> orderTableRequests = request.getOrderTables();
        validateOrderTableCount(orderTableRequests);

        List<OrderTable> orderTables = findOrderTables(orderTableRequests);
        validateOrderTableAllExists(orderTableRequests, orderTables);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        tableGroupRepository.save(tableGroup);

        changeOrderTables(orderTables, tableGroup);

        return TableGroupResponse.from(tableGroup);
    }

    private void validateOrderTableCount(List<OrderTableRequest> orderTableRequests) {
        if (CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> findOrderTables(List<OrderTableRequest> orderTableRequests) {
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private void validateOrderTableAllExists(List<OrderTableRequest> orderTableRequests, List<OrderTable> orderTables) {
        if (orderTableRequests.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void changeOrderTables(List<OrderTable> orderTables, TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateIsEmpty();
            orderTable.validateTableGroupNotExists();

            orderTable.changeTableGroup(tableGroup);
            orderTable.changeEmpty(false);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        validateOngoingOrderNotExists(orderTables);

        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
            orderTable.changeEmpty(false); // TODO: ?
            orderTableRepository.save(orderTable);
        }
    }

    private void validateOngoingOrderNotExists(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
