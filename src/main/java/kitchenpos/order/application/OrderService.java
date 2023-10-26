package kitchenpos.order.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.dao.OrderedMenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreatedEvent;
import kitchenpos.order.dto.OrderDto;
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
    private final OrderedMenuDao orderedMenuDao;

    public OrderService(ApplicationEventPublisher eventPublisher, OrderDao orderDao, OrderLineItemDao orderLineItemDao, OrderedMenuDao orderedMenuDao) {
        this.eventPublisher = eventPublisher;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderedMenuDao = orderedMenuDao;
    }

    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        for (final OrderLineItem orderLineItem : orderDto.getOrderLineItems()) {
            orderLineItemDao.save(orderLineItem);
        }

        Order order = new Order(orderDto.getOrderTableId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderDto.getOrderLineItems());

        validateMenu(order);
        eventPublisher.publishEvent(OrderCreatedEvent.from(order));
        return OrderDto.from(order);
    }

    private void validateMenu(Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        final List<Long> orderedMenuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != orderedMenuDao.countByIdIn(orderedMenuIds)) {
            throw new IllegalArgumentException();
        }
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
