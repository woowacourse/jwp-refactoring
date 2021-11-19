package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.domain.order.Tables;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTables = tableGroupRequest.getOrderTableRequests();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = getOrderTableIds(orderTables);

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIsIn(orderTableIds);
        Tables tables = new Tables(orderTableRepository.findAllByIdIsIn(orderTableIds));
        tables.validateSize(orderTables.size());
        tables.validateCondition();

        TableGroup tableGroup = new TableGroup();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        tables.changeCondition(tableGroup);

        List<OrderTableResponse> orderTableResponses = getOrderTableResponses(savedOrderTables);

        return new TableGroupResponse(
                savedTableGroup.getId(),
                savedTableGroup.getCreatedDate(),
                orderTableResponses
        );
    }

    private List<Long> getOrderTableIds(List<OrderTableRequest> orderTables) {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    private List<OrderTableResponse> getOrderTableResponses(List<OrderTable> savedOrderTables) {
        List<OrderTableResponse> orderTableResponses = new ArrayList<>();
        for (OrderTable orderTable : savedOrderTables) {
            OrderTableResponse orderTableResponse = new OrderTableResponse(orderTable.getId(),
                    orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
            orderTableResponses.add(orderTableResponse);
        }
        return orderTableResponses;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        Orders orders = new Orders(orderRepository.findAllByOrderTableIn(orderTables));
        orders.validateChangeEmpty();
        Tables tables = new Tables(orderTables);
        tables.changeCondition(null);
    }
}
