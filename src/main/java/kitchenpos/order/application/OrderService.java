package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.support.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableValidator orderTableValidator;

    public OrderService(
            MenuDao menuDao,
            OrderDao orderDao,
            OrderTableValidator orderTableValidator
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableValidator = orderTableValidator;
    }

    public OrderResponse create(Order request) {
        validateOrderLineItems(request);

        Long orderTableId = orderTableValidator.validateOrderTable(request.getOrderTableId());

        Order order = orderDao.save(Order.of(orderTableId, OrderStatus.COOKING, request.getOrderLineItems()));
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
        Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("상태를 변화시키기 위한 주문이 없습니다."));

        savedOrder.changeOrderStatus(orderStatus);
        Order order = orderDao.updateStatus(savedOrder);

        return new OrderResponse(order);
    }
}
