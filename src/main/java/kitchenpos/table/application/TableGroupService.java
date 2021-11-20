package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.Tables;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.request.TableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
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

        tables.changeCondition(tableGroup.getId());

        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    private List<Long> getOrderTableIds(List<OrderTableRequest> orderTables) {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = orderTables.stream()
                .map(orderTable -> orderTable.getId())
                .collect(Collectors.toList());
        Orders orders = new Orders(orderRepository.findAllByOrderTableIdIn(orderTableIds));
        orders.validateChangeEmpty();
        Tables tables = new Tables(orderTables);
        tables.changeCondition(null);
    }
}
