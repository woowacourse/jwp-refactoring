package kitchenpos.order;

import kitchenpos.order.request.OrderCreateRequest;
import kitchenpos.order.request.OrderLineItemCreateRequest;
import kitchenpos.order.request.OrderUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderDao orderDao;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderDao orderDao, OrderLineItemRepository orderLineItemRepository, OrderValidator orderValidator) {
        this.orderDao = orderDao;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(OrderCreateRequest request) {
        List<OrderLineItemCreateRequest> orderLineItems = request.getOrderLineItems();

        Order order = OrderMapper.mapToOrder(request, orderValidator);
        final Order savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest orderLineItemCreateRequest : orderLineItems) {
            OrderLineItem orderLineItem = mapToOrderLineItem(orderLineItemCreateRequest, orderId);
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemCreateRequest orderLineItem, Long orderId) {
        return OrderLineItem.of(
                orderId,
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderUpdateRequest request) {
        final Order order = getOrder(orderId);
        order.changeOrderStatus(request.getOrderStatus());

        orderDao.save(order);
        order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return order;
    }

    private Order getOrder(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
