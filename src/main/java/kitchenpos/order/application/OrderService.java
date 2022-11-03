package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.el.MethodNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.dao.MenuDao;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderSaveRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
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

    public OrderService(MenuDao menuDao, OrderDao orderDao, OrderLineItemDao orderLineItemDao, OrderTableDao orderTableDao) {
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
        List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
            .map(it -> it.toEntity(order.getId(), toOrderMenu(it.getMenuId())))
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
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new InvalidOrderLineItemCreateException();
        }
    }

    private OrderMenu toOrderMenu(Long menuId) {
        Menu menu = findMenu(menuId);
        return OrderMenu.of(menu);
    }

    public List<OrderResponse> list() {
        return orderDao.findAll().stream()
            .map(this::toOrderResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, String orderStatus) {
        Order order = findOrder(orderId);
        Order updatedOrder = orderDao.save(order.changeOrderStatus(OrderStatus.valueOf(orderStatus)));
        return toOrderResponse(updatedOrder);
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

    private Menu findMenu(Long menuId) {
        return menuDao.findById(menuId)
            .orElseThrow(MethodNotFoundException::new);
    }
}
