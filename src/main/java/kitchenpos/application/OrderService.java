package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
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
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderLineItems orderLineItems = new OrderLineItems(toOrderLineItems(orderRequest));
        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId());
        orderTable.validateTableIsFull();

        Order entity = new Order(null,
                orderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderLineItems.getItems());

        return toOrderResponse(orderDao.save(entity));
    }

    private OrderResponse toOrderResponse(Order savedOrder) {
        return new OrderResponse(savedOrder.getId(), savedOrder.getOrderTableId(), savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(), toOrderLineItemResponses(savedOrder));
    }

    private List<OrderLineItem> toOrderLineItems(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems().stream()
                .map(itemRequest -> new OrderLineItem(null, null, itemRequest.getMenuId(), itemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private List<OrderLineItemResponse> toOrderLineItemResponses(Order savedOrder) {
        return savedOrder.getOrderLineItems().stream()
                .map(orderLineItem -> new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity())).collect(
                        Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest statusRequest) {
        final Order order = orderDao.findById(orderId);
        order.updateOrderStatus(statusRequest.getOrderStatus().name());
        Order save = orderDao.save(order);
        return toOrderResponse(save);
    }
}
