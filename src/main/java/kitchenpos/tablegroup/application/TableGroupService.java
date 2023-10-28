package kitchenpos.tablegroup.application;

import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;

    private final OrderRepository orderRepository;
    private final TableService tableService;

    public TableGroupService(
            OrderRepository orderRepository,
            TableService tableService,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();
        OrderTables savedOrderTables = tableService.findAllByTableIds(orderTableIds);
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }

        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.from(savedOrderTables));
        savedTableGroup.assignTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        OrderTables orderTables = new OrderTables(tableService.findAllByTableGroupId(tableGroupId));
        validateOrderStatus(orderTables);
        orderTables.ungroup();
    }

    private void validateOrderStatus(OrderTables orderTables) {
        List<Long> orderTableIds = orderTables.getIds();
        List<OrderStatus> invalidOrderStatusToUngroup = List.of(OrderStatus.COOKING, OrderStatus.MEAL);
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, invalidOrderStatusToUngroup)) {
            throw new IllegalArgumentException();
        }
    }
}
