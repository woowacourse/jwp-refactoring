package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dao.MenuDao;
import kitchenpos.application.dao.OrderDao;
import kitchenpos.application.dao.OrderLineItemDao;
import kitchenpos.application.dao.OrderTableDao;
import kitchenpos.application.request.OrderChangeStatusRequest;
import kitchenpos.application.request.OrderRequest;
import kitchenpos.application.request.OrderRequest.OrderLineItemRequest;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
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

    public OrderService(final MenuDao menuDao, final OrderDao orderDao, final OrderLineItemDao orderLineItemDao,
                        final OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        OrderTable orderTable = getOrderTable(request);
        Order order = orderDao.save(Order.from(orderTable));
        List<OrderLineItem> orderLineItems = createOrderLineItems(request.getOrderLineItems(), order);
        orderLineItems.forEach(orderLineItemDao::save);
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 목록이 비어있습니다.");
        }
        return OrderResponse.of(order, orderLineItems);
    }

    private OrderTable getOrderTable(final OrderRequest request) {
        return orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }

    private List<OrderLineItem> createOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests,
                                                     final Order order) {
        return orderLineItemRequests.stream()
                .map((it) -> {
                    Menu menu = menuDao.findById(it.getMenuId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다."));
                    return new OrderLineItem(order.getId(), menu.getName(), menu.getPrice(), it.getQuantity());
                })
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
