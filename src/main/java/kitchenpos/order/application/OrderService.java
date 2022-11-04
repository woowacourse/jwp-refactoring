package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderChangeRequest;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderResponse create(final OrderCreateRequest dto) {
        final Long orderTableId = findOrderTableId(dto.getOrderTableId());
        final List<OrderLineItem> orderLineItems = getOrderLineItems(dto);
        final Order order = new Order(orderTableId, LocalDateTime.now(), orderLineItems);
        final Order savedOrder = orderDao.save(order);

        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(orderLineItems, savedOrder.getId());

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(),
                savedOrder.getOrderTableId(),
                savedOrderLineItems
        );
    }

    private List<OrderLineItem> saveOrderLineItems(List<OrderLineItem> orderLineItems, Long orderId) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.associateOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return savedOrderLineItems;
    }

    private List<OrderLineItem> getOrderLineItems(OrderCreateRequest dto) {
        final List<OrderLineItem> orderLineItems = dto.getOrderLineItems().stream()
                .map(orderLineItemDto -> new OrderLineItem(orderLineItemDto.getMenuId(), orderLineItemDto.getMenuId()))
                .collect(Collectors.toList());
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        validateIsCorrectOrderLineItemSize(orderLineItems, menuIds);
        return orderLineItems;
    }

    private void validateIsCorrectOrderLineItemSize(List<OrderLineItem> orderLineItems, List<Long> menuIds) {
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private Long findOrderTableId(Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        return orderTable.getId();
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getOrderStatus(),
                        order.getOrderedTime(),
                        order.getOrderTableId(),
                        orderLineItemDao.findAllByOrderId(order.getId())
                )).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeRequest dto) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeStatus(dto.getOrderStatus());
        orderDao.save(savedOrder);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(),
                savedOrder.getOrderTableId(),
                orderLineItemDao.findAllByOrderId(orderId)
        );
    }
}
