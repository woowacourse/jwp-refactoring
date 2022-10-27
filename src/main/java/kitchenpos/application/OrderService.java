package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.CreateOrderRequest;
import kitchenpos.dto.order.CreateOrderLineItemRequest;
import kitchenpos.dto.order.ChangeOrderStatusRequest;

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
        final List<Long> menuIds = request.getOrderLineItems().stream()
            .map(item -> item.getMenuId())
            .collect(Collectors.toList());

        if (menuIds.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }

        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블 입니다."));

        final Order savedOrder = orderDao.save(new Order(orderTable.getId()));

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (CreateOrderLineItemRequest item : request.getOrderLineItems()) {
            final OrderLineItem orderLineItem = new OrderLineItem(orderId, item.getMenuId(), item.getQuantity());
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 주문입니다."));

        savedOrder.changeStatus(request.getStatus());
        orderDao.save(savedOrder);
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
