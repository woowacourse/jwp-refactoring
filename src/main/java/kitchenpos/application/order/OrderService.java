package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderCreationRequest;
import kitchenpos.application.dto.OrderItemsWithQuantityRequest;
import kitchenpos.application.dto.OrderStatusChangeRequest;
import kitchenpos.application.dto.result.OrderResult;
import kitchenpos.dao.order.OrderLineItemRepository;
import kitchenpos.dao.order.OrderRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderMenuExistValidationEvent;
import kitchenpos.domain.order.OrderTableExistValidationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final ApplicationEventPublisher eventPublisher

    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResult create(final OrderCreationRequest request) {
        final List<OrderItemsWithQuantityRequest> orderLineItemRequests = request.getOrderLineItems();
        final Long orderTableId = request.getOrderTableId();
        eventPublisher.publishEvent(new OrderTableExistValidationEvent(orderTableId));
        final Order order = orderRepository.save(new Order(orderTableId));
        final List<OrderLineItem> orderLineItems = getOrderLineItemsByRequest(order, orderLineItemRequests);
        orderLineItemRepository.saveAll(orderLineItems);
        return OrderResult.from(order);
    }

    private List<OrderLineItem> getOrderLineItemsByRequest(
            final Order order,
            final List<OrderItemsWithQuantityRequest> orderLineItemRequests
    ) {
        final List<Long> menuIds = extractMenuIds(orderLineItemRequests);
        eventPublisher.publishEvent(new OrderMenuExistValidationEvent(menuIds));
        return orderLineItemRequests.stream().map(orderItemRequest ->
                new OrderLineItem(order, orderItemRequest.getMenuId(), orderItemRequest.getQuantity())
        ).collect(Collectors.toList());
    }

    private List<Long> extractMenuIds(final List<OrderItemsWithQuantityRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderItemsWithQuantityRequest::getMenuId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResult> list() {
        return orderRepository.findAll().stream()
                .map(OrderResult::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResult changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order existOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        existOrder.changeOrderStatus(request.getOrderStatus());
        return OrderResult.from(orderRepository.save(existOrder));
    }
}
