package kitchenpos.order;

import kitchenpos.order.OrderDao;
import kitchenpos.order.OrderLineItemDao;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.OrderDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderService(ApplicationEventPublisher eventPublisher, OrderDao orderDao, OrderLineItemDao orderLineItemDao) {
        this.eventPublisher = eventPublisher;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        for (final OrderLineItem orderLineItem : orderDto.getOrderLineItems()) {
            orderLineItemDao.save(orderLineItem);
        }

        Order order = new Order(orderDto.getOrderTableId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderDto.getOrderLineItems());
        eventPublisher.publishEvent(order);
        return OrderDto.from(order);
    }

    public List<OrderDto> list() {
        final List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(order -> new Order(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
                        order.getOrderedTime(),
                        orderLineItemDao.findAllByOrderId(order.getId())))
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderDto orderDto) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(orderDto.getOrderStatus());
        savedOrder.changeStatus(orderStatus);

        List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(orderId);
        Order order = new Order(savedOrder.getId(), savedOrder.getOrderStatus(), savedOrder.getOrderedTime(), orderLineItems);

        orderDao.save(order);
        return OrderDto.from(order);
    }
}
