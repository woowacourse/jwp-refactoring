package kitchenpos.application;

import kitchenpos.domain.order.*;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusChangeRequest;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.util.ValidateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderLineItemAssembler orderLineItemAssembler;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(OrderLineItemAssembler orderLineItemAssembler, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.orderLineItemAssembler = orderLineItemAssembler;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        ValidateUtil.validateNonNull(orderCreateRequest.getOrderTableId());
        Long orderTableId = orderCreateRequest.getOrderTableId();
        OrderTable orderTable =
                orderTableRepository.findById(orderTableId).orElseThrow(() -> new OrderTableNotFoundException(orderTableId));

        Order order = Order.from(orderTable);
        Order savedOrder = orderRepository.save(order);

        OrderLineItemDtos orderLineItemDtos = orderCreateRequest.toOrderLineItemDtos();
        List<OrderLineItem> orderLineItems = orderLineItemAssembler.createOrderLineItems(orderLineItemDtos, order);
        orderLineItems.forEach(orderLineItemRepository::save);

        savedOrder.setOrderLineItems(orderLineItems);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> listAllOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(order -> order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId())));

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusChangeRequest orderStatusChangeRequest) {
        Order savedOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        OrderStatus orderStatus = orderStatusChangeRequest.toOrderStatus();
        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.from(savedOrder);
    }
}
