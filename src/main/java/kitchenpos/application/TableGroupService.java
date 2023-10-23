package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.exception.orderTableException.InvalidOrderTableException;
import kitchenpos.exception.tableGroupException.TableGroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = new TableGroup(getOrderTables(tableGroupRequest));

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    private OrderTables getOrderTables(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTables().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        final List<OrderTable> orderTables = orderTableRepository.findByIdIn(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new InvalidOrderTableException();
        }

        return new OrderTables(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);

        orderRepository.findByOrderTableIn(tableGroup.getOrderTables()).stream()
                .forEach(Order::validateOrderComplete);

        tableGroup.ungroup();
    }
}
