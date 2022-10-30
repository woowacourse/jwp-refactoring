package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.dao.MenuDao;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderSaveRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.dao.OrderDao;
import kitchenpos.order.domain.dao.OrderLineItemDao;
import kitchenpos.order.exception.InvalidOrderLineItemCreateException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.dao.OrderTableDao;
import kitchenpos.table.exception.OrderTableNotFoundException;
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
    public OrderResponse create(OrderSaveRequest request) {
        Long orderTableId = request.getOrderTableId();
        validateOrderTable(orderTableId);
        Order order = orderDao.save(Order.from(orderTableId));
        List<OrderLineItem> orderLineItems = request.toOrderLineItemsEntities(order.getId()).stream()
            .map(orderLineItemDao::save)
            .collect(Collectors.toList());
        validateOrderLineItems(orderLineItems);
        return OrderResponse.toResponse(order, orderLineItems);
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = findOrderTable(orderTableId);
        orderTable.validateNotEmpty();
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems) || isSavedMenus(orderLineItems)) {
            throw new InvalidOrderLineItemCreateException();
        }
    }

    private boolean isSavedMenus(List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
        return orderLineItems.size() != menuDao.countByIdIn(menuIds);
    }

    public List<OrderResponse> list() {
        return orderDao.findAll().stream()
            .map(this::toOrderResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, String orderStatus) {
        Order order = findOrder(orderId);
        order.changeOrderStatus(OrderStatus.valueOf(orderStatus));
        orderDao.save(order);
        return toOrderResponse(order);
    }

    private OrderResponse toOrderResponse(Order order) {
        return OrderResponse.toResponse(order, orderLineItemDao.findAllByOrderId(order.getId()));
    }

    private Order findOrder(Long orderId) {
        return orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
            .orElseThrow(OrderTableNotFoundException::new);
    }
}
