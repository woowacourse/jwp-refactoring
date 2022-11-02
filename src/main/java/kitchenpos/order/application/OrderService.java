package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.request.OrderLineItemRequest;
import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.dao.OrderMenuDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderValidator;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderMenuDao orderMenuDao;
    private final MenuDao menuDao;

    public OrderService(final OrderDao orderDao, final OrderTableDao orderTableDao,
        final OrderLineItemDao orderLineItemDao, final OrderMenuDao orderMenuDao,
        final MenuDao menuDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderMenuDao = orderMenuDao;
        this.menuDao = menuDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final OrderTable orderTable = orderTableDao.getById(request.getOrderTableId());
        validateOrderTableNotEmpty(orderTable);

        Order order = orderDao.save(new Order(orderTable));

        OrderValidator orderValidator = new OrderValidator();
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(request);
        orderValidator.validateOrderLineItems(orderLineItems);
        saveOrderLineItems(orderLineItems, order);

        return OrderResponse.from(order);
    }

    private List<OrderLineItem> mapToOrderLineItems(final OrderRequest request) {
        List<OrderLineItem> list = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : request.getOrderLineItemRequests()) {
            Menu menu = menuDao.getById(orderLineItemRequest.getMenuId());
            OrderMenu orderMenu = orderMenuDao.save(new OrderMenu(menu.getName(), menu.getPrice()));
            OrderLineItem orderLineItem = new OrderLineItem(orderMenu.getId(), orderLineItemRequest.getQuantity());
            list.add(orderLineItem);
        }
        return Collections.unmodifiableList(list);
    }

    private void saveOrderLineItems(final List<OrderLineItem> orderLineItems, final Order order) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItemDao.save(
                new OrderLineItem(order.getId(), orderLineItem.getOrderMenuId(), orderLineItem.getQuantity())
            );
        }
    }

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        return OrderResponse.fromAll(orderDao.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderDao.getById(orderId);
        savedOrder.changeStatus(request.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}
