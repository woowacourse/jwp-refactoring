package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.ui.dto.OrderCreateRequest;
import kitchenpos.order.ui.dto.OrderLineItemDto;
import kitchenpos.order.ui.dto.OrderUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional(readOnly = true)
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
        validateOrderLineItems(request.getOrderLineItems());

        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        validateOrderTableNotEmpty(orderTable);

        final Order order = saveOrder(request, orderTable);
        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(order.getId(), request);
        order.addOrderLineItems(savedOrderLineItems);

        return OrderResponse.of(order);
    }

    private void validateOrderLineItems(final List<OrderLineItemDto> orderLineItemDtos) {
        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private Order saveOrder(final OrderCreateRequest requestOrder, final OrderTable orderTable) {
        Order order = new Order(
                orderTable.getId(),
                OrderStatus.COOKING,
                LocalDateTime.now(),
                requestOrder.toOrderLineItems()
        );
        return orderDao.save(order);
    }

    private List<OrderLineItem> saveOrderLineItems(final Long orderId, final OrderCreateRequest orderCreateRequest) {
        final List<OrderLineItem> result = new ArrayList<>();

        for (final OrderLineItemDto orderLineItem : orderCreateRequest.getOrderLineItems()) {
            OrderLineItem updateOrderLineItem = new OrderLineItem(
                    orderId,
                    orderLineItem.getMenuId(),
                    orderLineItem.getQuantity()
            );
            result.add(orderLineItemDao.save(updateOrderLineItem));
        }
        return result;
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.addOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return toRequest(orders);
    }

    private List<OrderResponse> toRequest(final List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order order = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        order.changeOrderStatus(orderStatus);

        Order savedOrder = orderDao.save(order);
        savedOrder.addOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return OrderResponse.of(savedOrder);
    }
}
