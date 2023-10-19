package kitchenpos.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import kitchenpos.order.exception.OrderLineEmptyException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.repository.OrderLineItemRepository;
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
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
            OrderTableRepository orderTableRepository, OrderLineItemRepository orderLineItemRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        Order order = saveOrder(request);
        setupOrderLineItems(request, order);
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrder(order);

        return OrderResponse.from(order, orderLineItems);
    }

    private Order saveOrder(CreateOrderRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);
        orderTable.validateIsNotEmpty();
        Order order = new Order(request.getOrderTableId(), OrderStatus.COOKING, LocalDateTime.now());

        return orderRepository.save(order);
    }

    private void setupOrderLineItems(CreateOrderRequest request, Order order) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : request.getOrderLineItems()) {
            OrderLineItem orderLineItem = saveOrderLineItem(order, orderLineItemRequest);
            orderLineItems.add(orderLineItem);
        }
        validateOrderLineNotEmpty(orderLineItems);
    }

    private OrderLineItem saveOrderLineItem(Order order, OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                .orElseThrow(MenuNotFoundException::new);
        OrderLineItem orderLineItem = new OrderLineItem(order, menu, orderLineItemRequest.getQuantity());

        return orderLineItemRepository.save(orderLineItem);
    }

    private void validateOrderLineNotEmpty(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new OrderLineEmptyException();
        }
    }

    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(each -> OrderResponse.from(each, orderLineItemRepository.findAllByOrder(each)))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, ChangeOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrder(order);

        return OrderResponse.from(order, orderLineItems);
    }
}
