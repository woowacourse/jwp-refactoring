package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.jpa.OrderRepository;
import kitchenpos.dao.jpa.OrderTableRepository;
import kitchenpos.dao.jpa.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.CreateTableGroupRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.TableGroupResponse;
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
    public TableGroupResponse create(final CreateTableGroupRequest request) {
        List<OrderTableRequest> orderTableRequests = request.getOrderTables();

        validateOrderTableSize(orderTableRequests);

        List<OrderTable> orderTables = findOrderTables(orderTableRequests);
        validateOrderTableAllExists(orderTableRequests, orderTables);

        for (final OrderTable savedOrderTable : orderTables) {
            validateOrderTableIsEmptyAndTableGroupIsNull(savedOrderTable);
        }

        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroupRepository.save(tableGroup);

        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(tableGroup);
            orderTable.changeEmpty(false);
        }

        return TableGroupResponse.from(tableGroup);
    }

    private void validateOrderTableIsEmptyAndTableGroupIsNull(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableAllExists(List<OrderTableRequest> orderTableRequests, List<OrderTable> orderTables) {
        if (orderTableRequests.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> findOrderTables(List<OrderTableRequest> orderTableRequests) {
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private void validateOrderTableSize(List<OrderTableRequest> orderTableRequests) {
        if (CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < 2) {
            throw new IllegalArgumentException();
        }
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
            orderTable.changeTableGroup(null);
            orderTable.changeEmpty(false); // TODO: ?
            orderTableRepository.save(orderTable);
        }
    }
}
