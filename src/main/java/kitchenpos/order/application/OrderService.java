package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.event.VerifiedMenusEvent;
import kitchenpos.event.VerifiedOrderTableEvent;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.application.OrderLineItemDto;
import kitchenpos.order.dto.request.ChangeOrderStatusRequest;
import kitchenpos.order.dto.request.CreateOrderLineItemRequest;
import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.repository.OrderRepository;

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
    public Order create(final CreateOrderRequest request) {
        validateCreateOrderRequest(request);

        List<OrderLineItemDto> orderLineItems = request.getOrderLineItems().stream()
            .map(it -> new OrderLineItemDto(it.getMenuId(), it.getQuantity()))
            .collect(Collectors.toList());

        return orderRepository.save(new Order(request.getOrderTableId(), orderLineItems));
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = findOrderById(orderId);
        order.changeStatus(request.getStatus());

        return order;
    }

    private void validateCreateOrderRequest(final CreateOrderRequest request) {
        publisher.publishEvent(new VerifiedOrderTableEvent(request.getOrderTableId()));

        List<Long> menuIds = request.getOrderLineItems().stream()
            .map(CreateOrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        publisher.publishEvent(new VerifiedMenusEvent(menuIds));
    }

    private Order findOrderById(final Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 주문입니다."));
    }
}
