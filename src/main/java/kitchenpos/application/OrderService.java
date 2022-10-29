package kitchenpos.application;

import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public OrderResponse create(final OrderCreateRequest request) {
        final Long orderTableId = request.getOrderTableId();
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final Order order = new Order(
                orderTable.getId(),
                request.getOrderLineItemRequests()
                        .stream()
                        .map(orderLineItemRequest -> new OrderLineItem(
                                orderLineItemRequest.getMenuId(),
                                orderLineItemRequest.getQuantity()
                        )).collect(Collectors.toList())
        );
        order.checkMenuSize(menuDao.countByIdIn(order.getMenuIds()));
        return OrderResponse.from(orderDao.save(order));
    }

    public List<OrderResponse> list() {
        return orderDao.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(orderDao.save(savedOrder));
    }
}
