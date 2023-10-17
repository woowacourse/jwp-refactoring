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
import kitchenpos.dto.request.order.ChangeOrderRequest;
import kitchenpos.dto.request.order.CreateOrderRequest;
import kitchenpos.dto.request.order.OrderLineItemsDto;
import kitchenpos.dto.response.OrderResponse;
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
    public OrderResponse create(final CreateOrderRequest request) {
        final List<OrderLineItemsDto> orderLineItemDtos = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemDtos.stream()
                .map(OrderLineItemsDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order order = Order.builder()
                .orderTableId(request.getOrderTableId())
                .orderStatus(OrderStatus.COOKING.name())
                .orderedTime(LocalDateTime.now())
                .build();

        final Long orderId = orderDao.save(order).getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        final List<OrderLineItem> orderLineItems = orderLineItemDtos.stream()
                .map(OrderLineItem::from)
                .collect(Collectors.toList());
        for (final OrderLineItem orderLineItem : orderLineItems) {
            final OrderLineItem newOrderLineItem = new OrderLineItem(
                    orderLineItem.getSeq(),
                    orderId,
                    orderLineItem.getMenuId(),
                    orderLineItem.getQuantity()
            );
            savedOrderLineItems.add(orderLineItemDao.save(newOrderLineItem));
        }

        return OrderResponse.from(Order.builder()
                .id(orderId)
                .orderTableId(order.getOrderTableId())
                .orderStatus(order.getOrderStatus())
                .orderedTime(order.getOrderedTime())
                .orderLineItems(savedOrderLineItems)
                .build()
        );
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(order -> Order.builder()
                        .id(order.getId())
                        .orderTableId(order.getOrderTableId())
                        .orderStatus(order.getOrderStatus())
                        .orderedTime(order.getOrderedTime())
                        .orderLineItems(orderLineItemDao.findAllByOrderId(order.getId()))
                        .build()
                )
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());

        final Order order = Order.builder()
                .id(orderId)
                .orderTableId(savedOrder.getOrderTableId())
                .orderStatus(orderStatus.name())
                .orderedTime(savedOrder.getOrderedTime())
                .orderLineItems(orderLineItemDao.findAllByOrderId(orderId))
                .build();

        orderDao.save(order);

        return OrderResponse.from(order);
    }
}
