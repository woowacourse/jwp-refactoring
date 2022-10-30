package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.response.OrderResponse;
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
        final Order order = Order.from(request.getOrderTableId(), request.getOrderLineItems(),
                menuDao.countByIdIn(getMenuIds(request)));
        final Order savedOrder = orderDao.save(order);
        final List<OrderLineItem> savedOrderLineItems = getOrderLineItems(request, savedOrder.getId());
        return OrderResponse.of(savedOrder, savedOrderLineItems);
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<Long> getMenuIds(final OrderCreateRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return orderDao.findAll()
                .stream()
                .map(it -> OrderResponse.of(it, orderLineItemDao.findAllByOrderId(it.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId).orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(order.getOrderStatus());
        orderDao.save(savedOrder);
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return OrderResponse.of(orderDao.save(savedOrder), orderLineItemDao.findAllByOrderId(orderId));
    }

    private List<OrderLineItem> getOrderLineItems(OrderCreateRequest request, Long orderId) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : request.getOrderLineItems()) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return savedOrderLineItems;
    }
}
