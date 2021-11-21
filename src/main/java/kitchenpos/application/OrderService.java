package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = orderRequest.toOrder();
        validateOrderTable(order);

        order.startCooking();
        orderDao.save(order);
        updateOrderLineItems(order);

        return OrderResponse.of(order);
    }

    private void validateOrderTable(Order order) {
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        orderTable.validateNotEmpty();
    }

    private void updateOrderLineItems(Order order) {
        final OrderLineItems orderLineItems = order.getOrderLineItems();
        final List<Long> menuIds = orderLineItems.getMenuIds();

        orderLineItems.validateSameSize(menuDao.countByIdIn(menuIds));
        orderLineItems.updateOrderInfo(order);
        orderLineItemDao.saveAll(orderLineItems.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        final OrderStatus orderStatus = orderRequest.getOrderStatus();

        order.changeOrderStatus(orderStatus);
        return OrderResponse.of(order);
    }
}
