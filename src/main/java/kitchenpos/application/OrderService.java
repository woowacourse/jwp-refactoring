package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderChangeStatusRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
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
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = request.getOrderLineItems();
        validateEmptyOrderLineItem(request.getOrderLineItems());
        validateOrderLineItems(orderLineItems);
        validateOrderTable(request.getOrderTableId());

        final Order savedOrder = saveOrder(request);

        final List<OrderLineItem> savedOrderLineItems = getOrderLineItems(orderLineItems, savedOrder);
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return OrderResponse.toResponse(savedOrder);
    }

    private List<OrderLineItem> getOrderLineItems(final List<OrderLineItem> orderLineItems, final Order savedOrder) {
        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return savedOrderLineItems;
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("[ERROR] 메뉴의 수와 실제 주문한 메뉴의 수가 다릅니다.");
        }
    }

    private void validateEmptyOrderLineItem(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("[ERROR] 주문 항목이 비어있습니다.");
        }
    }

    private void validateOrderTable(final Long orderTableId) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 주문 테이블이 비어있습니다.");
        }
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 주문 테이블입니다."));
    }

    private Order saveOrder(final OrderCreateRequest request) {
        return orderDao.save(
                new Order(
                        request.getOrderTableId(),
                        OrderStatus.COOKING.name(),
                        request.getOrderedTime(),
                        request.getOrderLineItems()
                )
        );
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(order -> OrderResponse.toResponse(order, orderLineItemDao.findAllByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.toResponse(saveOrder(savedOrder, orderId));
    }

    private Order findOrderById(final Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 주문입니다."));
    }

    private Order saveOrder(final Order order, final Long orderId) {
        return orderDao.save(
                new Order(
                        order.getId(),
                        order.getOrderStatus(),
                        order.getOrderedTime(),
                        orderLineItemDao.findAllByOrderId(orderId)
                )
        );
    }
}
