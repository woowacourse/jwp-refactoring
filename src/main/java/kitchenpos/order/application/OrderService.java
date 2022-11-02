package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.event.VerifiedMenusEvent;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.application.OrderLineItemDto;
import kitchenpos.order.dto.request.ChangeOrderStatusRequest;
import kitchenpos.order.dto.request.CreateOrderLineItemRequest;
import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.event.VerifiedOrderTableEvent;
import kitchenpos.order.repository.OrderRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher publisher;

    public OrderService(OrderRepository orderRepository, ApplicationEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    @Transactional
    public OrderResponse create(final CreateOrderRequest request) {
        validateCreateOrderRequest(request);

        List<OrderLineItemDto> orderLineItems = request.getOrderLineItems().stream()
            .map(it -> new OrderLineItemDto(it.getMenuId(), it.getQuantity()))
            .collect(Collectors.toList());

        Order order = orderRepository.save(new Order(request.getOrderTableId(), orderLineItems));

        return new OrderResponse(order);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 주문입니다."));

        order.changeStatus(request.getStatus());

        return new OrderResponse(order);
    }

    private void validateCreateOrderRequest(final CreateOrderRequest request) {
        publisher.publishEvent(new VerifiedOrderTableEvent(request.getOrderTableId()));

        final List<Long> menuIds = request.getOrderLineItems().stream()
            .map(CreateOrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        publisher.publishEvent(new VerifiedMenusEvent(menuIds));
    }

}
