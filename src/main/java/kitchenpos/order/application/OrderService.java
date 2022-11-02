package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.application.OrderLineItemDto;
import kitchenpos.order.dto.request.ChangeOrderStatusRequest;
import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.repository.OrderRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final CreateOrderRequest request) {
        orderValidator.validateCreateOrder(request);

        final List<OrderLineItemDto> orderLineItems = request.getOrderLineItems().stream()
            .map(it -> new OrderLineItemDto(it.getMenuId(), it.getQuantity()))
            .collect(Collectors.toList());

        final Order order = orderRepository.save(new Order(request.getOrderTableId(), orderLineItems));

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

}
