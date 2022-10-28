package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.request.OrderRequest;
import kitchenpos.application.response.OrderResponse;
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
        List<Long> menuIds = getMenuIds(orderLineItems);
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }
        OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
        return createOrder(orderLineItems, orderTable);
    }

    private List<Long> getMenuIds(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private OrderResponse createOrder(final List<OrderLineItem> orderLineItems, final OrderTable orderTable) {
        Order order = orderDao.save(Order.of(orderTable));
        Long orderId = order.getId();
        return OrderResponse.of(order, createOrderLineItems(orderLineItems, orderId));
    }

    private static List<OrderLineItem> createOrderLineItems(final List<OrderLineItem> orderLineItems,
                                                            final Long orderId) {
        return orderLineItems.stream()
                .map(it -> OrderLineItem.of(it.getSeq(), orderId, it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Order> list() {
        List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(it -> Order.of(
                        it.getOrderTableId(),
                        it.getOrderStatus(),
                        it.getOrderedTime(),
                        orderLineItemDao.findAllByOrderId(it.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order가 존재하지 않습니다."));
        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException("주문이 이미 완료되었습니다.");
        }
        OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        orderDao.save(savedOrder);
        return new Order(savedOrder.getId(), savedOrder.getOrderTableId(), orderStatus.name(), LocalDateTime.now(),
                orderLineItemDao.findAllByOrderId(orderId));
    }
}
