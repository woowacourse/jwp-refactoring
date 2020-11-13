package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.order.repository.OrderLineItemDao;
import kitchenpos.order_table.repository.OrderTableDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
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
        List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        validateNotEmptyOrderLineItems(orderLineItemRequests);

        OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        List<Menu> menus = findMenus(orderLineItemRequests);
        List<OrderLineItem> orderLineItems = convertOrderLineItems(orderLineItemRequests, menus);

        Order order = Order.builder()
            .orderTable(orderTable)
            .orderLineItems(orderLineItems)
            .build();

        Order savedOrder = orderDao.save(order);
        orderLineItemDao.saveAll(orderLineItems);

        return OrderResponse.from(savedOrder);
    }

    private void validateNotEmptyOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
    }

    private List<Menu> findMenus(final List<OrderLineItemRequest> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
        List<Menu> menus = menuDao.findAllById(menuIds);
        validateSavedMenu(menuIds, menus);

        return menus;
    }

    private void validateSavedMenu(final List<Long> menuIds, final List<Menu> menus) {
        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> convertOrderLineItems(
        final List<OrderLineItemRequest> requests,
        final List<Menu> menus
    ) {
        return requests.stream()
            .map(request -> convertOrderLineItem(request, menus))
            .collect(Collectors.toList());
    }

    private OrderLineItem convertOrderLineItem(
        final OrderLineItemRequest request,
        final List<Menu> menus
    ) {
        Menu menu = findMenu(request, menus);
        return OrderLineItem.builder()
            .menu(menu)
            .build();
    }

    private Menu findMenu(final OrderLineItemRequest request, final List<Menu> menus) {
        return menus.stream()
            .filter(menu -> menu.isSameId(request.getMenuId()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public List<OrderResponse> list() {
        List<Order> orders = orderDao.findAll();
        return OrderResponse.listFrom(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(
        final Long orderId,
        final OrderStatusChangeRequest request
    ) {
        Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(request.getOrderStatus());

        Order changedOrder = orderDao.save(savedOrder);
        return OrderResponse.from(changedOrder);
    }
}
