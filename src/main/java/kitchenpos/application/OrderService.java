package kitchenpos.application;

import java.time.LocalDateTime;
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
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderLineItemCreateRequest;
import kitchenpos.ui.request.OrderStatusChangeRequest;
import kitchenpos.ui.response.OrderResponse;
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
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                                                          .map(this::createOrderLineItem)
                                                          .collect(Collectors.toList());

        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                                                   .orElseThrow(EntityNotFoundException::new);

        final var order = new Order(orderTable, orderLineItems, LocalDateTime.now());
        return OrderResponse.from(orderDao.save(order));
    }

    private OrderLineItem createOrderLineItem(final OrderLineItemCreateRequest orderLineItemCreateRequest) {
        final Menu menu = findMenuById(orderLineItemCreateRequest);
        return new OrderLineItem(menu, orderLineItemCreateRequest.getQuantity());
    }

    private Menu findMenuById(final OrderLineItemCreateRequest orderLineItemCreateRequest) {
        return menuDao.findById(orderLineItemCreateRequest.getMenuId())
                      .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderDao.findAll().stream()
                       .map(OrderResponse::from)
                       .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order order = orderDao.findById(orderId)
                                    .orElseThrow(EntityNotFoundException::new);
        final var orderStatus = OrderStatus.valueOf(request.getOrderStatus());

        order.changeStatusTo(orderStatus);
        return OrderResponse.from(order);
    }
}
