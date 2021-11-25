package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.ui.dto.request.OrderChangeStatusRequest;
import kitchenpos.order.ui.dto.request.OrderCreatedRequest;
import kitchenpos.order.ui.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderCreatedRequest request) {
        orderValidator.validateTable(request.getOrderTableId());
        final Order savedOrder = orderRepository.save(Order.create(request.getOrderTableId()));

        final List<OrderLineItem> orderLineItems = request.getOrderLineItemRequests()
                .stream()
                .map(orderLineItemRequest -> {
                    orderValidator.validateMenu(orderLineItemRequest.getMenuId());
                    return OrderLineItem.create(savedOrder, orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity());
                })
                .collect(toList());

        List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.saveAll(orderLineItems);
        return OrderResponse.create(savedOrder, savedOrderLineItems);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(order -> OrderResponse.create(order, orderLineItemRepository.findAllByOrder(order)))
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest orderChangeStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Id : " + orderId + "인 주문이 존재하지 않습니다."));
        savedOrder.validateCompletion();

        savedOrder.changeOrderStatus(orderChangeStatusRequest.getOrderStatus());
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrder(savedOrder);

        return OrderResponse.create(savedOrder, orderLineItems);
    }
}
