package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ChangeOrderStatusCommand;
import kitchenpos.application.dto.CreateOrderCommand;
import kitchenpos.application.dto.CreateOrderCommand.OrderLineItemRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final CreateOrderCommand command) {
        final List<OrderLineItemRequest> orderLineItemRequests = command.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = command.getMenuIds();
        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }


        final OrderTable orderTable = orderTableDao.findById(command.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        // todo: order repository save (return orderRepository.save(order))
        Order order = new Order(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), null);
        final Order savedOrder = orderDao.save(order);
        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = orderLineItemRequests.stream()
                .map(request -> {
                    final OrderLineItem orderLineItem = new OrderLineItem();
                    orderLineItem.setOrderId(orderId);
                    orderLineItem.setMenuId(request.getMenuId());
                    orderLineItem.setQuantity(request.getQuantity());
                    return orderLineItemDao.save(orderLineItem);
                })
                .collect(Collectors.toList());
        savedOrder.setOrderLineItems(savedOrderLineItems);
        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        // todo: repository findAllByOrderIdIn (return orderRepository.findAll())
        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }
        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final ChangeOrderStatusCommand command) {
        final Long orderId = command.getOrderId();
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(command.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        // todo: repository save (return orderRepository.save(order))
        orderDao.save(savedOrder);
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
        return savedOrder;
    }
}
