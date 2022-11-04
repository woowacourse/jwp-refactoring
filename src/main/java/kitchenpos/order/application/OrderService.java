package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            MenuDao menuDao,
            OrderDao orderDao,
            OrderLineItemDao orderLineItemDao,
            OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    public OrderResponse create(Order request) {
        validateOrderLineItems(request);

        OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문을 생성하기 위한 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비었습니다");
        }

        Order order = orderDao.save(Order.of(orderTable.getId(), OrderStatus.COOKING, request.getOrderLineItems()));
        return new OrderResponse(order);
    }

    private void validateOrderLineItems(Order request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems();

        if (orderLineItems.size() != menuDao.countByIdIn(getMenuIds(orderLineItems))) {
            throw new IllegalArgumentException("주문정보가 존재하지 않습니다.");
        }
    }

    private List<Long> getMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderDao.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("상태를 변화시키기 위한 주문이 없습니다."));

        savedOrder.changeOrderStatus(orderStatus);
        savedOrder.changeOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return new OrderResponse(orderDao.save(savedOrder));
    }
}
