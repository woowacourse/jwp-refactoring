package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.ui.OrderCreateRequest;
import kitchenpos.ui.OrderLineItemCreateRequest;
import kitchenpos.ui.request.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                                                          .map(
                                                                  orderLineItemCreateRequest -> new OrderLineItem(
                                                                          findMenuById(orderLineItemCreateRequest),
                                                                          orderLineItemCreateRequest.getQuantity()
                                                                  )
                                                          )
                                                          .collect(Collectors.toList());

        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                                                   .orElseThrow(EntityNotFoundException::new);

        final var order = new Order(orderTable, orderLineItems);
        return orderDao.save(order);
    }

    private Menu findMenuById(final OrderLineItemCreateRequest orderLineItemCreateRequest) {
        return menuDao.findById(orderLineItemCreateRequest.getMenuId())
                      .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order order = orderDao.findById(orderId)
                                         .orElseThrow(EntityNotFoundException::new);
        final var orderStatus = OrderStatus.valueOf(request.getOrderStatus());

        order.changeStatusTo(orderStatus);
        return order;
    }
}
