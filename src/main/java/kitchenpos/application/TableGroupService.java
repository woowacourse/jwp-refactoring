package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTables()
                .stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        List<OrderTable> orderTables = orderTableRepository.getAllByIdIn(orderTableIds);

        TableGroup tableGroup = new TableGroup(orderTables);

        tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTablesOfTableGroup = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Order> ordersOfTableGroup = orderRepository.findAllByOrderTableIn(orderTablesOfTableGroup);
        ordersOfTableGroup.forEach(Order::validateOrderStatusIsCompletion);

        orderTablesOfTableGroup.forEach(OrderTable::ungroup);
    }
}
