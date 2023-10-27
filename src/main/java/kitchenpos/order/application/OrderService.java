package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.order.ui.request.OrderCreateRequest;
import kitchenpos.order.ui.request.OrderLineItemCreateRequest;
import kitchenpos.order.ui.request.OrderStatusChangeRequest;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableDao;
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

        final var order = orderTable.order(orderLineItems, LocalDateTime.now());
        return OrderResponse.from(orderDao.save(order));
    }

    private OrderLineItem createOrderLineItem(final OrderLineItemCreateRequest orderLineItemCreateRequest) {
        final Menu menu = findMenuById(orderLineItemCreateRequest);
        return new OrderLineItem(menu.getId(), orderLineItemCreateRequest.getQuantity(), menu.getName(), menu.getPrice());
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
