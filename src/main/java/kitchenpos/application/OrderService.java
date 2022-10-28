package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.ChangeOrderStatusRequest;
import kitchenpos.dto.order.CreateOrderRequest;

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
    public Order create(final CreateOrderRequest request) {
        final OrderTable orderTable = findOrderTableById(request.getOrderTableId());

        final Map<Menu, Long> orderLineItems = request.getOrderLineItems().stream()
            .collect(Collectors.toMap(
                it -> findMenuById(it.getMenuId()),
                it -> it.getQuantity()
            ));

        Order order = new Order(orderTable, orderLineItems);
        return orderDao.save(order);
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();
        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = findOrderById(orderId);
        order.changeStatus(request.getStatus());

        return order;
    }

    private Menu findMenuById(Long id) {
        return menuDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 입니다."));
    }

    private Order findOrderById(Long id) {
        return orderDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 주문입니다."));
    }
}
