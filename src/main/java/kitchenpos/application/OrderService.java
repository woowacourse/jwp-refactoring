package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.Menus;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderChangeRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        OrderLineItems orderLineItems
            = new OrderLineItems(orderCreateRequest.getOrderLineItems());
        Menus menus = new Menus(orderLineItems.toMenus());
        validateMenuIds(menus);

        final OrderTable orderTable = orderTableRepository
            .findById(orderCreateRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        final Order savedOrder = saveOrder(orderTable);
        List<OrderLineItemResponse> orderLineItemResponses
            = createOrderLineItemResponses(orderLineItems, savedOrder);

        return OrderResponse.of(savedOrder, orderLineItemResponses);
    }

    private void validateMenuIds(Menus menus) {
        if (!menus.isSameSize(menuRepository.countByIdIn(menus.extractIds()))) {
            throw new IllegalArgumentException();
        }
    }

    private Order saveOrder(OrderTable orderTable) {
        Order order = Order.of(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        return orderRepository.save(order);
    }

    private List<OrderLineItemResponse> createOrderLineItemResponses(
        OrderLineItems orderLineItems, Order savedOrder) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems.getOrderLineItems()) {
            OrderLineItem newOrderLineItem
                = new OrderLineItem(savedOrder.getId(), savedOrder, orderLineItem.getMenu(),
                orderLineItem.getQuantity());
            OrderLineItem savedOrderLineItem = orderLineItemRepository.save(newOrderLineItem);

            savedOrderLineItems.add(savedOrderLineItem);
        }

        return OrderLineItemResponse.toResponseList(savedOrderLineItems);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        final List<Order> ordersWithLineItems = new ArrayList<>();

        for (final Order order : orders) {
            Order orderWithLineItem
                = new Order(order.getId(), order.getOrderTable(),
                order.getOrderStatus(), order.getOrderedTime(),
                orderLineItemRepository.findAllByOrderId(order.getId()));

            ordersWithLineItems.add(orderWithLineItem);
        }

        return OrderResponse.toResponseList(ordersWithLineItems);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final OrderChangeRequest orderChangeRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        validateStatus(savedOrder);

        final OrderStatus orderStatus = OrderStatus.valueOf(orderChangeRequest.getOrderStatus());

        Order changedOrder
            = new Order(savedOrder.getId(), savedOrder.getOrderTable(), orderStatus,
            savedOrder.getOrderedTime(), orderLineItemRepository.findAllByOrderId(orderId));

        orderRepository.save(changedOrder);

        return OrderResponse.of(changedOrder);
    }

    private void validateStatus(Order savedOrder) {
        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }
}
