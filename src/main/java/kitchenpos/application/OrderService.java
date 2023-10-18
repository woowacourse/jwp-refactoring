package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.exception.MenuNotFoundException;
import kitchenpos.application.exception.OrderNotFoundException;
import kitchenpos.application.exception.OrderTableNotFoundException;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.CreateOrderLineItemRequest;
import kitchenpos.ui.dto.request.CreateOrderRequest;
import kitchenpos.ui.dto.request.UpdateOrderStatusRequest;
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
    public Order create(final CreateOrderRequest request) {
        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                                                   .orElseThrow(OrderTableNotFoundException::new);
        final List<OrderLineItem> orderLineItems = findOrderLineItems(request);
        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);

        return orderDao.save(order);
    }

    private List<OrderLineItem> findOrderLineItems(final CreateOrderRequest request) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (final CreateOrderLineItemRequest orderLineItemRequest : request.getOrderLineItems()) {
            final Menu menu = menuDao.findById(orderLineItemRequest.getMenuId())
                                     .orElseThrow(MenuNotFoundException::new);

            orderLineItems.add(new OrderLineItem(menu, orderLineItemRequest.getQuantity()));
        }

        return orderLineItems;
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final UpdateOrderStatusRequest request) {
        final Order persistOrder = orderDao.findById(orderId)
                                           .orElseThrow(OrderNotFoundException::new);
        persistOrder.updateOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return persistOrder;
    }
}
