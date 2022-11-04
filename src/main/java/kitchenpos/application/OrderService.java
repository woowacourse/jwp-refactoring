package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Order create(final OrderRequest request) {
        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final List<OrderLineItem> orderLineItems = findOrderLineItems(request.getOrderLineItems());
        final Order order = request.toEntity(orderLineItems);
        orderTable.add(order);
        return order;
    }

    private List<OrderLineItem> findOrderLineItems(final List<OrderLineItemRequest> orderLineItemsRequest) {
        final List<Long> menuIds = orderLineItemsRequest.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        final List<Menu> savedMenus = menuDao.findAllByIdIn(menuIds);
        if (menuIds.size() != savedMenus.size()) {
            throw new IllegalArgumentException();
        }
        return orderLineItemsRequest.stream()
                .map(it -> it.toEntity(matchMenu(it.getMenuId(), savedMenus)))
                .collect(Collectors.toList());
    }

    private Menu matchMenu(final Long menuId, final List<Menu> savedMenus) {
        return savedMenus.stream()
                .filter(it -> it.getId().equals(menuId))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.updateOrderStatus(request.getOrderStatus());
        orderDao.save(savedOrder);
        return savedOrder;
    }
}
