package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.domain.TableGroupValidator;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.ui.dto.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        final List<Long> requestOrderTableIds = request.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllById(requestOrderTableIds);

        TableGroupValidator.validateGroupOrderTables(orderTables, requestOrderTableIds);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.setOrderTables(orderTables);

        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단체 지정입니다. 단체 지정을 삭제할 수 없습니다."));
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        final List<Order> orders = orderTables.stream()
                .flatMap(orderTable -> orderRepository.findByOrderTable(orderTable).stream())
                .collect(Collectors.toList());
        TableGroupValidator.validateUngroupTableOrders(orders);

        tableGroup.unGroup();
    }
}
