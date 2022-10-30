package kitchenpos.application;

import static java.util.stream.Collectors.*;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderSaveRequest;
import kitchenpos.dto.OrderChangeOrderStatusRequest;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderRepository orderRepository;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderRepository orderRepository) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderSaveRequest request) {
        Order savedOrder = orderRepository.save(new Order(request.getOrderTableId(), toOrderLineItems(request)));
        return new OrderResponse(savedOrder);
    }

    private List<OrderLineItem> toOrderLineItems(final OrderSaveRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.addOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders.stream()
                .map(OrderResponse::new)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeOrderStatusRequest request) {
        final Order savedOrder = orderRepository.getById(orderId);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        savedOrder.addOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return new OrderResponse(savedOrder);
    }
}
