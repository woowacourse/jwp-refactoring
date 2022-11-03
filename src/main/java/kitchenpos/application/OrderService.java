package kitchenpos.application;

import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderValidator;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(OrderValidator orderValidator, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository) {
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final OrderLineItems orderLineItems = createOrderLineItemsByRequest(orderRequest);

        orderValidator.validateOrderTableEmpty(orderRequest.getOrderTableId());

        final Order order = orderRepository.save(new Order(null, orderRequest.getOrderTableId(),
                orderRequest.getOrderStatus(), orderRequest.getOrderedTime(), orderLineItems.getOrderLineItems()));

        return new Order(order.getId(), orderRequest.getOrderTableId(), order.getOrderStatus(),
                order.getOrderedTime(), createOrderLineItems(orderLineItems, order).getOrderLineItems());
    }

    private OrderLineItems createOrderLineItems(OrderLineItems orderLineItems, Order order) {
        return new OrderLineItems(orderLineItems.getOrderLineItems()
                .stream()
                .map(orderLineItem -> orderLineItemRepository.save(new OrderLineItem(order,
                        orderLineItem.getName(), orderLineItem.getPrice(), orderLineItem.getQuantity())))
                .collect(Collectors.toList()));
    }

    private OrderLineItems createOrderLineItemsByRequest(OrderRequest orderRequest) {
        final OrderLineItems orderLineItems = new OrderLineItems(orderRequest.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(null, it.getName(), it.getPrice(), it.getQuantity()))
                .collect(Collectors.toList()));

        orderValidator.validateMenuExists(orderLineItems.getOrderLineItems(), mapToMenuIds(orderRequest));
        return orderLineItems;
    }

    private List<Long> mapToMenuIds(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.validateStatusForChange();

        return orderRepository.save(new Order(savedOrder.getId(), savedOrder.getOrderTableId(),
                order.getOrderStatus(), savedOrder.getOrderedTime(),
                new OrderLineItems(orderLineItemRepository.findAllByOrderId(orderId)).getOrderLineItems()));
    }
}
