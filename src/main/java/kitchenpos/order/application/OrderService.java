package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    @Transactional
    public Order create(final Order order) {
        final List<Long> menuIds = order.extractMenuIds();
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        orderTable.validateNotEmpty();

        order.changeStatus(OrderStatus.COOKING);

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeStatus(order.getOrderStatus());

        return orderRepository.save(savedOrder);
    }

    public OrderTables findAllByIdIn(final List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
        return new OrderTables(orderTables);
    }

    public OrderTables findAllByTableGroupId(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        return new OrderTables(orderTables);
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
        final List<OrderStatus> orderStatuses) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }
}
