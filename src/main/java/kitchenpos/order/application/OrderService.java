package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.request.CreateOrderRequest;
import kitchenpos.order.application.dto.request.OrderLineItemRequest;
import kitchenpos.order.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest createOrderRequest) {
        OrderTable orderTable = orderTableRepository.findById(createOrderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now());

        List<OrderLineItemRequest> orderLineItemRequests = createOrderRequest.getOrderLineItems();
        addOrderLineItems(orderLineItemRequests, order);

        long orderMenuSize = extractOrderMenuCount(orderLineItemRequests);
        order.checkEqualMenuCount(orderMenuSize);

        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    private long extractOrderMenuCount(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        return menuRepository.countByIdIn(menuIds);
    }

    private void addOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests, Order order) {
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), orderLineItemRequest.getQuantity());
            order.addOrderLineItem(orderLineItem);
        }
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, UpdateOrderStatusRequest updateOrderStatusRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (order.isCompleted()) {
            throw new IllegalArgumentException();
        }

        OrderStatus orderStatus = OrderStatus.valueOf(updateOrderStatusRequest.getOrderStatus());
        order.changeOrderStatus(orderStatus);

        return OrderResponse.from(order);
    }
}
