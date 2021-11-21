package kitchenpos.order.application;

import kitchenpos.exception.BadRequestException;
import kitchenpos.exception.ErrorType;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.order.ui.request.OrderStatusModifyRequest;
import kitchenpos.order.ui.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderOrderTableValidator orderOrderTableValidator;
    private final OrderMenuConnector orderMenuConnector;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderOrderTableValidator orderOrderTableValidator,
            final OrderMenuConnector orderMenuConnector
    ) {
        this.orderRepository = orderRepository;
        this.orderOrderTableValidator = orderOrderTableValidator;
        this.orderMenuConnector = orderMenuConnector;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderOrderTableValidator.validateOrderTable(orderRequest.getOrderTableId());
        final List<OrderLineItem> orderLineItems = generateOrderLineItems(orderRequest.getOrderLineItems());
        final Order order = new Order.Builder()
                .orderTableId(orderRequest.getOrderTableId())
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.now())
                .orderLineItems(orderLineItems)
                .build();

        orderRepository.save(order);
        return OrderResponse.of(order);
    }

    private List<OrderLineItem> generateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            orderMenuConnector.validateMenuId(orderLineItemRequest.getMenuId());
            final OrderMenu orderMenu = orderMenuConnector.generateOrderMenu(orderLineItemRequest.getMenuId());

            final OrderLineItem orderLineItem = new OrderLineItem.Builder()
                    .orderMenu(orderMenu)
                    .quantity(orderLineItemRequest.getQuantity())
                    .build();
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAllFetchJoinOrderLineItems();
        return OrderResponse.toList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusModifyRequest orderStatusModifyRequest) {
        final Order foundOrder = findOrder(orderId);
        foundOrder.changeOrderStatus(OrderStatus.valueOf(orderStatusModifyRequest.getOrderStatus()));
        return OrderResponse.of(foundOrder);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ErrorType.ORDER_NOT_FOUND));
    }
}
