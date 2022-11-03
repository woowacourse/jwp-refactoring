package kitchenpos.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.exception.MenuNotFoundException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderValidator orderValidator) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests = orderCreateRequest.getOrderLineItems();
        List<OrderLineItem> orderLineItems = addOrderLineItem(orderLineItemCreateRequests);
        Order order = Order.newOrder(orderCreateRequest.getOrderTableId(), orderLineItems, orderValidator);
        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder);
    }

    private List<OrderLineItem> addOrderLineItem(List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemCreateRequest orderLineItemCreateRequest : orderLineItemCreateRequests) {
            Menu menu = menuRepository.findById(orderLineItemCreateRequest.getMenuId())
                    .orElseThrow(MenuNotFoundException::new);
            OrderLineItem orderLineItem = new OrderLineItem(menu.getName(),
                    menu.getPrice(), new Quantity(orderLineItemCreateRequest.getQuantity()));
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderStatusChangeRequest orderCreateRequest) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        OrderStatus orderStatus = OrderStatus.from(orderCreateRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);
        return savedOrder;
    }
}
