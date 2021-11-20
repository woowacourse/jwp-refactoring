package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.Tables;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.repository.TableGroupRepository;
import kitchenpos.order.dto.request.OrderTableRequest;
import kitchenpos.order.dto.request.TableGroupRequest;
import kitchenpos.order.dto.response.TableGroupResponse;
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
        Orders orders = new Orders(orderRepository.findAllByOrderTableIn(orderTables));
        orders.validateChangeEmpty();
        Tables tables = new Tables(orderTables);
        tables.changeCondition(null);
    }
}
