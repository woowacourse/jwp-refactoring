package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.dao.MenuDao;
import kitchenpos.order.application.request.OrderChangeStatusRequest;
import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.dao.OrderDao;
import kitchenpos.order.domain.dao.OrderLineItemDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.dao.OrderTableDao;
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
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = request.createOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 목록이 비어있습니다.");
        }
        validateOrderLineItems(orderLineItems);
        OrderTable orderTable = getOrderTable(request);
        return createOrder(orderLineItems, orderTable);
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = getMenuIds(orderLineItems);
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }
    }

    private List<Long> getMenuIds(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private OrderTable getOrderTable(final OrderRequest request) {
        return orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }

    private OrderResponse createOrder(final List<OrderLineItem> orderLineItems, final OrderTable orderTable) {
        Order order = orderDao.save(Order.from(orderTable));
        Long orderId = order.getId();
        return OrderResponse.of(order, createOrderLineItems(orderLineItems, orderId));
    }

    private List<OrderLineItem> createOrderLineItems(final List<OrderLineItem> orderLineItems,
                                                            final Long orderId) {
        return orderLineItems.stream()
                .map(it -> new OrderLineItem(it.getSeq(), orderId, it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(it -> OrderResponse.of(it, orderLineItemDao.findAllByOrderId(it.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order가 존재하지 않습니다."));
        order.changeStatus(request.getOrderStatus());
        Order savedOrder = orderDao.save(order);
        return OrderResponse.of(savedOrder, savedOrder.getOrderLineItems());
    }
}
