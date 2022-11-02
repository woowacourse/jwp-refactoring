package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.event.ValidateExistMenusEvent;
import kitchenpos.order.application.dto.request.OrderLineItemRequest;
import kitchenpos.order.application.dto.request.OrderRequest;
import kitchenpos.order.application.dto.request.OrderStatusUpdateRequest;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderService(OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository,
                        OrderTableRepository orderTableRepository,
                        ApplicationEventPublisher applicationEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public OrderResponse create(OrderRequest request) {
        List<OrderLineItem> orderLineItems = getOrderLineItems(request.getOrderLineItems());
        validateOrderItemSize(orderLineItems);

        OrderTable orderTable = findOrderTable(request.getOrderTableId());
        Order order = orderRepository.save(new Order(orderTable, OrderStatus.COOKING));

        for (OrderLineItem orderLineItem : orderLineItems) {
            order.addOrderLineItem(orderLineItem);
            orderLineItemRepository.save(orderLineItem);
        }

        return new OrderResponse(order);
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        validateExistMenus(orderLineItemRequests);

        return orderLineItemRequests.stream()
                .map(o -> new OrderLineItem(o.getMenuId(), o.getQuantity()))
                .collect(Collectors.toList());
    }

    private void validateExistMenus(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        applicationEventPublisher.publishEvent(new ValidateExistMenusEvent(menuIds));
    }

    private void validateOrderItemSize(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order order = findOrder(orderId);

        OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        order.changeStatus(orderStatus);

        return new OrderResponse(order);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }
}
