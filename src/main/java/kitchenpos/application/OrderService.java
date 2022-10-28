package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.OrderCreateRequest;
import kitchenpos.application.request.OrderStatusChangeRequest;
import kitchenpos.dao.menu.MenuDao;
import kitchenpos.dao.order.OrderDao;
import kitchenpos.dao.order.OrderLineItemDao;
import kitchenpos.dao.table.OrderTableDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Order create(final OrderCreateRequest request) {
        Order order = new Order(
                parseOrderTableId(request.getOrderTableId()),
                request.getOrderLineItems()
                        .stream()
                        .map(orderLineItemCreateRequest -> new OrderLineItem(orderLineItemCreateRequest.getMenuId(), orderLineItemCreateRequest.getQuantity()))
                        .collect(Collectors.toList()));
        order.validateMenuSize(menuDao.countByIdIn(order.menuIds()));
        return orderDao.save(order);
    }

    private Long parseOrderTableId(final Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문테이블이 비어있습니다.");
        }
        return orderTable.getId();
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return orderDao.save(savedOrder);
    }
}
