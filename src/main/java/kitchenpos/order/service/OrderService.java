package kitchenpos.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.request.ChangeOrderStatusRequest;
import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
            OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        Order order = saveOrder(request);
        setupOrderLineItems(request, order);

        return OrderResponse.from(order, order.getOrderLineItems());
    }

    private Order saveOrder(CreateOrderRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);
        orderTable.validateIsNotEmpty();
        Order order = new Order(request.getOrderTableId(), OrderStatus.COOKING, LocalDateTime.now());

        return orderRepository.save(order);
    }

    private void setupOrderLineItems(CreateOrderRequest request, Order order) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems()
                .stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList());

        order.setupOrderLineItems(orderLineItems);
    }

    private OrderLineItem createOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                .orElseThrow(MenuNotFoundException::new);

        return new OrderLineItem(menu.getId(), orderLineItemRequest.getQuantity());
    }

    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(each -> OrderResponse.from(each, each.getOrderLineItems()))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, ChangeOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return OrderResponse.from(order, order.getOrderLineItems());
    }

    public void validateOrdersCompleted(Long orderTableId) {
        List<Order> orders = orderRepository.findByOrderTableId(orderTableId);
        for (Order order : orders) {
            order.validateOrderIsCompleted();
        }
    }
}
