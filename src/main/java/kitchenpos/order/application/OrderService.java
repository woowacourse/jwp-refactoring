package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderUpdateStatusRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.ordertable.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderValidator orderValidator;

    public OrderService(
        final MenuDao menuDao,
        final OrderDao orderDao,
        final OrderLineItemDao orderLineItemDao,
        final OrderValidator orderValidator
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        validateNotDuplicateMenuId(request.getOrderLineItems());
        orderValidator.validateTableNotEmpty(request.getOrderTableId());

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
        final List<OrderLineItem> orderLineItems, final Long orderId
    ) {
        return orderLineItems.stream()
            .map(it -> {
                final Menu menu = menuDao.findById(it.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
                final OrderLineItem orderLineItem = new OrderLineItem(null, orderId,
                    it.getMenuId(), it.getQuantity(), menu.getName(), menu.getPrice());
                return orderLineItemDao.save(orderLineItem);
            })
            .collect(Collectors.toList());
    }

    private void validateNotDuplicateMenuId(final List<OrderLineItem> orderLineItems) {
        final OrderLineItems items = new OrderLineItems(orderLineItems);
        final List<Long> menuIds = items.getMenuIds();

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }
}
