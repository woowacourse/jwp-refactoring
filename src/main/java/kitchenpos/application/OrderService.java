package kitchenpos.application;

import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderCreateRequest.OrderLineItemRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.application.dto.OrderStatusChangeRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();

        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        final Order.OrderFactory orderFactory = new Order.OrderFactory(orderTable);
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            final Menu menu = menuDao.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            orderFactory.addMenu(menu, orderLineItemRequest.getQuantity());
        }
        final Order order = orderFactory.create();

        return OrderResponse.from(orderDao.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest statusChangeRequest) {
        final Order order = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(statusChangeRequest.getOrderStatus());
        order.changeOrderStatus(orderStatus);

        return OrderResponse.from(order);
    }
}
