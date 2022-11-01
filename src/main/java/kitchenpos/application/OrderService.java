package kitchenpos.application;

import kitchenpos.application.dto.CreateOrderDto;
import kitchenpos.application.dto.CreateOrderLineItemDto;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public OrderDto create(final CreateOrderDto createOrderDto) {
        final List<Long> menuIds = createOrderDto.getOrderLineItems().stream()
                .map(CreateOrderLineItemDto::getMenuId)
                .collect(Collectors.toList());

        final Order order = Order.create(createOrderDto.getOrderTableId(), menuIds);
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final Order createdOrder = Order.create(orderTable.getId(), menuIds);

        final Order savedOrder = orderRepository.save(createdOrder);

        final Long orderId = savedOrder.getId();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            final OrderLineItem savedOrderItem = orderLineItemRepository.save(orderLineItem);
            savedOrder.addMenu(savedOrderItem);
        }

        return OrderDto.of(savedOrder);
    }

    public List<OrderDto> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
            orderLineItems.forEach(order::addMenu);
        }

        return orders.stream()
                .map(OrderDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {

        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        savedOrder.changeOrderStatus(orderStatus.name());

        orderRepository.save(savedOrder);

        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);
        orderLineItems.forEach(savedOrder::addMenu);

        return OrderDto.of(savedOrder);
    }
}
