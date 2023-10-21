package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order_line_item.OrderLineItem;
import kitchenpos.domain.order_table.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final Order order) {
        final OrderLineItems orderLineItems = order.getOrderLineItems();

        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.getMenuIds();

        final int savedMenuSize = menuRepository.countByIdIn(menuIds);
        if (orderLineItems.isNotSameSize(savedMenuSize)) {
            throw new IllegalArgumentException();
        }

        if (Objects.isNull(order.getOrderTable().getId())) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTable().getId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.updateOrderStatus(OrderStatus.COOKING);

        orderRepository.save(order);

        for (final OrderLineItem orderLineItem : orderLineItems.getValues()) {
            orderLineItem.setOrder(order);
            orderLineItemRepository.save(orderLineItem);
        }

        return order;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        if (Objects.isNull(order.getId()) || !Objects.equals(orderId, order.getId())) {
            throw new IllegalArgumentException();
        }

        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrder.getOrderStatus().equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }

        orderRepository.save(order);

        return order;
    }
}
