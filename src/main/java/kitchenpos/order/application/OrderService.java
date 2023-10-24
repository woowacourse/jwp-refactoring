package kitchenpos.order.application;

import kitchenpos.common.event.listener.MenuEventListener;
import kitchenpos.common.event.listener.OrderTableEventListener;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.OrderUpdateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderLineItemEmptyException;
import kitchenpos.order.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuEventListener menuEventListener;
    private final OrderTableEventListener orderTableEventListener;

    public OrderService(final OrderRepository orderRepository, final MenuEventListener menuEventListener, final OrderTableEventListener orderTableEventListener) {
        this.orderRepository = orderRepository;
        this.menuEventListener = menuEventListener;
        this.orderTableEventListener = orderTableEventListener;
    }

    @Transactional
    public Order create(final OrderCreateRequest req) {
        validateOrderLineItemsEmpty(req);
        orderTableEventListener.validateOrderTable(req.getOrderTableId());

        List<OrderLineItem> orderLineItems = makeOrderLineItems(req.getOrderLineItems());
        validateOrderLineItemEmpty(req, orderLineItems);

        Order order = Order.createDefault(req.getOrderTableId(), orderLineItems);
        return orderRepository.save(order);
    }

    private void validateOrderLineItemsEmpty(final OrderCreateRequest req) {
        if (req.getOrderLineItems().isEmpty()) {
            throw new OrderLineItemEmptyException();
        }
    }

    private List<OrderLineItem> makeOrderLineItems(final List<OrderLineItemCreateRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        orderLineItemRequests.forEach(orderLineItemRequest -> {
            menuEventListener.validateMenuExist(orderLineItemRequest.getMenuId());
            orderLineItems.add(orderLineItemRequest.toDomain());
        });

        return orderLineItems;
    }

    private void validateOrderLineItemEmpty(final OrderCreateRequest req, final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() != req.getOrderLineItems().size()) {
            throw new OrderLineItemEmptyException();
        }
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderUpdateRequest req) {
        Order savedOrder = findOrder(orderId);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(req.getOrderStatus()).name());

        return savedOrder;
    }

    private Order findOrder(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }
}
