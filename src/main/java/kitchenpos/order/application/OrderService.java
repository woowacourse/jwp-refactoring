package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("주문메뉴가 존재하지 않습니다.");
        }

        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문테이블이 존재하지 않습니다."));

        final Order order = Order.newOrder(orderTable);

        final List<OrderLineItem> orderLineItems = convertToOrderLineItems(orderLineItemRequests, order);
        order.changeOrderLineItems(orderLineItems);
        orderDao.save(order);
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> convertToOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests, Order order) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> new OrderLineItem(order,
                        findMenu(orderLineItemRequest),
                        orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private OrderMenu findMenu(OrderLineItemRequest orderLineItemRequest) {
        final Menu menu = menuDao.findById(orderLineItemRequest.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("주문받은 메뉴가 실제 저장되어 있는 메뉴에 속하지 않습니다."));
        return new OrderMenu(menu.getId(), menu.getName(), menu.getPrice());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChangeOrderStatusResponse changeOrderStatus(final Long orderId,
                                                       final ChangeOrderStatusRequest changeOrderStatusRequest) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("저장되어 있지 않은 주문입니다."));

        savedOrder.changeStatus(OrderStatus.valueOf(changeOrderStatusRequest.getOrderStatus()));
        return ChangeOrderStatusResponse.from(savedOrder);
    }
}
