package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
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
    public Order create(final OrderDto orderDto) {
        final List<OrderLineItem> orderLineItems = orderDto.getOrderLineItems();
        validateOrderLineItems(orderLineItems);
        validateMenu(orderLineItems);
        final OrderTable orderTable = searchOrderTable(orderDto.toEntity());
        validateOrderTable(orderTable);
        Order orderForSave =
                Order.ofNullId(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), List.of());
        final Order savedOrder = orderDao.save(orderForSave);
        fillOrderIdToOrderLineItems(orderLineItems, savedOrder);
        return savedOrder;
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("[ERROR] 주문 목록이 존재하지 않습니다.");
        }
    }

    private void validateMenu(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() != menuDao.countByIdIn(getMenuIds(orderLineItems))) {
            throw new IllegalArgumentException("[ERROR] 메뉴가 존재하지 않습니다.");
        }
    }

    private List<Long> getMenuIds(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        return menuIds;
    }

    private OrderTable searchOrderTable(final Order order) {
        return orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 주문 테이블을 찾을 수 없습니다."));
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 주문 테이블이 비어있습니다.");
        }
    }

    private void fillOrderIdToOrderLineItems(final List<OrderLineItem> orderLineItems, final Order savedOrder) {
        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.addAllOrderLineItems(savedOrderLineItems);
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();
        for (final Order order : orders) {
            order.addAllOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }
        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderDto orderDto) {
        final Order savedOrder = searchOrder(orderId);
        validateOrderStatus(savedOrder);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderDto.getOrderStatus());
        savedOrder.updateOrderStatus(orderStatus.name());
        orderDao.save(savedOrder);
        savedOrder.addAllOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
        return savedOrder;
    }

    private Order searchOrder(final Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] Order 를 찾을 수 없습니다."));
    }

    private void validateOrderStatus(final Order savedOrder) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException("[ERROR] 주문이 이미 계산 완료 되었습니다.");
        }
    }
}
