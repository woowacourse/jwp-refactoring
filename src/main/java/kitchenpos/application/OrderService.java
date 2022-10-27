package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderUpdateStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public OrderResponse create(final OrderCreateRequest request) {
        validateNotDuplicateMenuId(request.getOrderLineItems());
        validateTableNotEmpty(request.getOrderTableId());

        final Order order = orderDao.save(request.toEntity());
        final List<OrderLineItem> orderLineItems = saveOrderLineItems(
            request.getOrderLineItems(), order.getId());

        return OrderResponse.of(order, orderLineItems);
    }

    public List<OrderResponse> list() {
        return orderDao.findAll()
            .stream()
            .map(it -> OrderResponse.of(it, orderLineItemDao.findAllByOrderId(it.getId())))
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(
        final Long orderId, final OrderUpdateStatusRequest request
    ) {
        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        final Order order = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
        order.updateOrderStatus(orderStatus.name());

        return OrderResponse.of(orderDao.save(order), orderLineItemDao.findAllByOrderId(orderId));
    }

    private List<OrderLineItem> saveOrderLineItems(
        final List<OrderLineItem> orderLineItems, final Long orderId) {
        return orderLineItems.stream()
            .map(it -> new OrderLineItem(null, orderId, it.getMenuId(), it.getQuantity()))
            .map(orderLineItemDao::save)
            .collect(Collectors.toList());
    }

    private void validateNotDuplicateMenuId(final List<OrderLineItem> orderLineItems) {
        final OrderLineItems items = new OrderLineItems(orderLineItems);
        final List<Long> menuIds = items.getMenuIds();

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTableNotEmpty(final Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
