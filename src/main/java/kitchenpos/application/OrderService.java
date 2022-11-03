package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
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
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(final OrderDao orderDao,
                        final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable table = orderTableDao.findById(orderRequest.getOrderTableId())
                .validateTableIsFull();

        Order order = new Order(table.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                toOrderLineItems(orderRequest));

        return toOrderResponse(orderDao.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest statusRequest) {
        final Order order = orderDao.findById(orderId)
                .placeOrderStatus(statusRequest.getOrderStatus().name());
        Order save = orderDao.save(order);
        return toOrderResponse(save);
    }

    private OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
                order.getOrderedTime(), toOrderLineItemResponses(order));
    }

    private OrderLineItems toOrderLineItems(OrderRequest orderRequest) {
        List<OrderLineItem> items = orderRequest.getOrderLineItems().stream()
                .map(itemRequest -> new OrderLineItem(itemRequest.getMenuId(), itemRequest.getQuantity()))
                .collect(Collectors.toList());
        return new OrderLineItems(items);
    }

    private List<OrderLineItemResponse> toOrderLineItemResponses(Order savedOrder) {
        return savedOrder.getOrderLineItems().stream()
                .map(orderLineItem -> new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity())).collect(
                        Collectors.toList());
    }
}
