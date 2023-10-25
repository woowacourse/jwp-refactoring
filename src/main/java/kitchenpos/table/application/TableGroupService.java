package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.request.TableGroupCreateRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
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
