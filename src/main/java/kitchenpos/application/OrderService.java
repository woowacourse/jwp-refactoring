package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusChangeRequest;
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
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        Order order = Order.builder()
            .orderTable(orderTable)
            .orderStatus(OrderStatus.COOKING.name())
            .orderedTime(LocalDateTime.now())
            .build();

        final Order savedOrder = orderDao.save(order);
        List<OrderLineItem> orderLineItems = convertOrderLineItems(orderLineItemRequests, order);
        orderLineItemDao.saveAll(orderLineItems);

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> convertOrderLineItems(List<OrderLineItemRequest> requests,
        Order order) {
        return requests.stream()
            .map(request -> convertOrderLineItem(request, order))
            .collect(Collectors.toList());
    }

    private OrderLineItem convertOrderLineItem(OrderLineItemRequest request, Order order) {
        Menu menu = menuDao.findById(request.getMenuId())
            .orElseThrow(IllegalArgumentException::new);

        return OrderLineItem.builder()
            .order(order)
            .menu(menu)
            .build();
    }

    @Transactional
    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        return OrderResponse.listFrom(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final OrderStatusChangeRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return OrderResponse.from(savedOrder);
    }
}
