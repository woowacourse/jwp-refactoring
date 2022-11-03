package kitchenpos.order.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.dto.request.OrderCreateRequest;
import kitchenpos.order.application.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
        final OrderTable orderTable = findOrderTable(request.getOrderTableId());
        orderTable.validateEmpty();
//        final Order order = Order.from(request.getOrderTableId(), request.getOrderLineItems(),
//                menuDao.countByIdIn(getMenuIds(request)));
        final Order order = saveOrder(request, orderTable);
        final Order savedOrder = orderDao.save(order);
        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(request, savedOrder.getId());
        return OrderResponse.of(savedOrder, savedOrderLineItems);
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Order saveOrder(final OrderCreateRequest request, final OrderTable orderTable) {
        Order order = Order.from(
                orderTable.getId(),
                toOrderLineItems(request),
                menuDao.countByIdIn(getMenuIds(request))
        );
        return orderDao.save(order);
    }

    private List<OrderLineItem> toOrderLineItems(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItemCreateRequest> orderLineItems = orderCreateRequest.getOrderLineItems();
        return orderLineItems.stream()
                .map(it -> {
                    Menu menu = menuDao.findById(it.getMenuId())
                            .orElseThrow(IllegalArgumentException::new);
                    return it.toEntity(menu);
                })
                .collect(Collectors.toList());
    }

    private List<Long> getMenuIds(final OrderCreateRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return orderDao.findAll()
                .stream()
                .map(it -> OrderResponse.of(it, orderLineItemDao.findAllByOrderId(it.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderCreateRequest order) {
        final Order savedOrder = orderDao.findById(orderId).orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(order.getOrderStatus());
        orderDao.save(savedOrder);
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return OrderResponse.of(orderDao.save(savedOrder), orderLineItemDao.findAllByOrderId(orderId));
    }

    private List<OrderLineItem> saveOrderLineItems(OrderCreateRequest request, Long orderId) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItemCreateRequest orderLineItem : request.getOrderLineItems()) {
            Menu menu = menuDao.findById(orderLineItem.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            OrderLineItem updateOrderLineItem = new OrderLineItem(
                    orderId,
                    orderLineItem.getQuantity(),
                    menu.getName(),
                    menu.getPrice()
            );
            savedOrderLineItems.add(orderLineItemDao.save(updateOrderLineItem));
        }
        return savedOrderLineItems;
    }
}
