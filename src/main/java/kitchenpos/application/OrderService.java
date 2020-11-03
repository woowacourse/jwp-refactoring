package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusChangeRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

        try {
            Order changedOrder = orderDao.save(savedOrder);
            return OrderResponse.from(changedOrder);
        } catch (ObjectOptimisticLockingFailureException e) {
            return changeOrderStatus(orderId, request);
        }
    }
}
