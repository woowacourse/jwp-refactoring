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
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemCreateRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusUpdateRequest;
import org.aspectj.weaver.ast.Or;
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
    public OrderResponse create(OrderCreateRequest request) {
        List<OrderLineItemCreateRequest> orderLineItemsRequest = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemsRequest)) {
            throw new IllegalArgumentException();
        }

        List<Long> menuIds = orderLineItemsRequest.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemsRequest.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order savedOrder = orderDao.save(request.toEntity(OrderStatus.COOKING.name(), LocalDateTime.now()));

        Long orderId = savedOrder.getId();
        List<OrderLineItem> savedOrderLineItems = orderLineItemsRequest.stream()
                .map(orderLineItem -> orderLineItemDao.save(new OrderLineItem(
                        orderId,
                        orderLineItem.getMenuId(),
                        orderLineItem.getQuantity()
                ))).collect(Collectors.toList());

        return OrderResponse.of(savedOrder, savedOrderLineItems);
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(order -> OrderResponse.of(order, orderLineItemDao.findAllByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());

        Order orderToUpdate = new Order(savedOrder.getId(), savedOrder.getOrderTableId(), orderStatus.name(),
                savedOrder.getOrderedTime(), orderLineItemDao.findAllByOrderId(orderId));

        Order updatedOrder = orderDao.save(orderToUpdate);

        return OrderResponse.from(updatedOrder);
    }
}
