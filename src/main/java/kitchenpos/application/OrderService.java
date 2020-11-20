package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.verifier.MenuVerifier;
import kitchenpos.domain.verifier.OrderTableVerifier;
import kitchenpos.dto.request.OrderChangeStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableVerifier orderTableVerifier;
    private final MenuVerifier menuVerifier;

    public OrderService(OrderRepository orderRepository,
        OrderLineItemRepository orderLineItemRepository,
        OrderTableVerifier orderTableVerifier, MenuVerifier menuVerifier) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableVerifier = orderTableVerifier;
        this.menuVerifier = menuVerifier;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        orderTableVerifier.verifyOrderTableByEmpty(request.getOrderTableId());
        List<Long> menuIds = request.getOrderLineItems().stream()
            .map(OrderLineItemCreateRequest::getMenuId)
            .collect(Collectors.toList());
        menuVerifier.verifyMenuCount(menuIds);

        final Order savedOrder = orderRepository.save(request.toEntity());
        final Long orderId = savedOrder.getId();

        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
            .map(item -> item.toEntity(orderId))
            .collect(Collectors.toList());
        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final OrderChangeStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.of(savedOrder);
    }
}
