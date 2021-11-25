package kitchenpos.tablegroup.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.exception.OrderTableNotFoundException;
import kitchenpos.tablegroup.exception.TableGroupNotFoundException;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.ui.request.TableGroupRequest;
import kitchenpos.tablegroup.ui.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(
        TableGroupRepository tableGroupRepository,
        OrderTableRepository orderTableRepository,
        OrderRepository orderRepository
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        OrderTables orderTables = generateOrderTables(request.getOrderTables());
        orderTables.validateEmpty();

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());
        orderTables.groupBy(tableGroup);

        return TableGroupResponse.from(tableGroup, orderTables.toList());
    }

    private OrderTables generateOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = new ArrayList<>();

        for (Long orderTableId : orderTableIds) {
            OrderTable orderTable = findOrderTableById(orderTableId);
            orderTables.add(orderTable);
        }

        return new OrderTables(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);

        for (OrderTable orderTable : orderTableRepository.findAllByTableGroup(tableGroup)) {
            Orders orders = new Orders(orderRepository.findAllByOrderTable(orderTable));
            orders.validateCompleted();

            orderTable.ungroup();
        }
    }

    public TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new TableGroupNotFoundException(
                String.format("%s ID에 해당하는 TableGroup이 존재하지 않습니다.", tableGroupId)
            ));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new OrderTableNotFoundException(
                String.format("%s ID에 해당하는 OrderTable이 존재하지 않습니다.", orderTableId)
            ));
    }
}
