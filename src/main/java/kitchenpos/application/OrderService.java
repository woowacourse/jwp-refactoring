package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderUpdateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrdersResponse;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = orderLineItems(request.getOrderLineItems());
        Order order = request.toEntity(orderLineItems);
        validateExistOrderTable(order);
        orderDao.save(order);
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> orderLineItems(final List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest request) {
        Menu menu = menuDao.findById(request.getMenuId())
                .orElseThrow(MenuNotFoundException::new);
        return new OrderLineItem(menu, request.getQuantity());
    }

    private void validateExistOrderTable(final Order order) {
        OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public OrdersResponse list() {
        List<Order> orders = orderDao.findAll();
        return OrdersResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}
