package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            @Qualifier("menuRepository") final MenuDao menuDao,
            @Qualifier("orderRepository") final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final Order order = proceedOrder(request);
        validateEmptyTable(order.getOrderTableId());
        validateOrderMenus(order);

        return OrderResponse.of(orderDao.save(order));
    }

    private static Order proceedOrder(final OrderRequest request) {
        final List<OrderLineItem> items = request.getOrderLineItems().stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());
        return Order.proceed(request.getOrderTableId(), items);
    }

    private void validateEmptyTable(final Long orderTableId) {
        if (getOrderTableById(orderTableId).isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTableById(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderMenus(final Order order) {
        if (order.getItemSize() != menuDao.countByIdIn(order.getMenuIds())) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        return orderDao.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = getOrderById(orderId);
        savedOrder.changeOrderStatus(request.getOrderStatus());
        orderDao.save(savedOrder);

        return OrderResponse.of(savedOrder);
    }

    private Order getOrderById(final Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
