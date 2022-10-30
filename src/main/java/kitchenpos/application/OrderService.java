package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderMenuRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
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
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = convertToOrder(orderRequest);
        validateMenusExist(orderRequest.getOrderLineItems().size(), order);
        validateOrderTableExistAndNotEmpty(orderRequest);
        orderDao.save(order);
        return OrderResponse.from(order);
    }

    private Order convertToOrder(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getOrderLineItems()
                .stream()
                .map(OrderMenuRequest::getMenuId)
                .collect(Collectors.toList());
        final List<Menu> menus = menuDao.findByIdIn(menuIds);
        return orderRequest.toEntity(menus);
    }

    private void validateMenusExist(final int menusSize, final Order order) {
        if (!order.isOrderLineItemsSizeEqualTo(menusSize)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableExistAndNotEmpty(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        validateOrderCompletion(savedOrder);
        savedOrder.updateOrderStatus(OrderStatus.valueOf(orderStatusRequest.getOrderStatus()));
        return OrderResponse.from(savedOrder);
    }

    private void validateOrderCompletion(final Order savedOrder) {
        if (savedOrder.isCompletion()) {
            throw new IllegalArgumentException();
        }
    }
}
