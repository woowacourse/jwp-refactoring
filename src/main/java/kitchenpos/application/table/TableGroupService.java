package kitchenpos.application.table;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.table.request.TableGroupRequest;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderTableRepository;
import kitchenpos.repository.table.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
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
    public TableGroup create(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.extractTableIds();
        List<OrderTable> finedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        OrderTables orderTables = OrderTables.of(finedOrderTables, orderTableIds.size());
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
        List<OrderTable> groupedTables = orderTables.group(savedTableGroup);
        orderTableRepository.saveAll(groupedTables);
        return savedTableGroup;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> finedOrderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        OrderTables orderTables = new OrderTables(finedOrderTables);
        List<Long> orderTableIds = orderTables.extractIds();
        validateOrderStatusCompletion(orderTableIds);
        List<OrderTable> ungroupedTables = orderTables.ungroup();
        orderTableRepository.saveAll(ungroupedTables);
    }

    private void validateOrderStatusCompletion(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중이나 식사중일 땐 테이블 그룹 해제가 불가능합니다.");
        }
    }

}
