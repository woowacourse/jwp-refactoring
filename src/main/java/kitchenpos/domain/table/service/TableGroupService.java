package kitchenpos.domain.table.service;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.service.dto.TableGroupCreateRequest;
import kitchenpos.domain.order.service.dto.TableGroupResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.repository.OrderTableRepository;
import kitchenpos.domain.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTables();
        final OrderTables orderTables = OrderTables.from(orderTableRepository.findAllByIdIn(orderTableIds));

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(now(), new OrderTables()));
        savedTableGroup.addOrderTables(orderTables.getOrderTables());

        return TableGroupResponse.toDto(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        final List<Order> savedOrders = orderRepository.findAllByOrderTableIds(orderTableIds);

        final boolean cannotUngroup = savedOrders.stream().anyMatch(Order::isProceeding);
        if (cannotUngroup) {
            throw new IllegalArgumentException("주문 상태가 식사중이거나 요리중이면 그룹을 해제할 수 없습니다.");
        }

        orderTables.forEach(OrderTable::ungroup);
    }
}
