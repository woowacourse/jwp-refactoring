package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        List<OrderLineItem> orderLineItems = toOrderLineItem(orderCreateRequest.getOrderLineItemDtos());
        OrderTable orderTable = orderTableDao.findById(orderCreateRequest.getOrderTableId()).orElseThrow(IllegalArgumentException::new);

        validate(orderLineItems, orderTable);

        Order orderToSave = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        final Order savedOrder = orderDao.save(orderToSave);

        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(savedOrder);
            orderLineItemDao.save(orderLineItem);
        }

        return OrderResponse.of(savedOrder);
    }

    private void validate(List<OrderLineItem> orderLineItems, OrderTable orderTable) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> toOrderLineItem(List<OrderLineItemDto> orderLineItemDtos) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            Menu menu = menuDao.findById(orderLineItemDto.getMenuId()).orElseThrow(IllegalArgumentException::new);
            orderLineItems.add(new OrderLineItem(menu, orderLineItemDto.getQuantity()));
        }
        return orderLineItems;
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = order.getOrderStatus();
        savedOrder.setOrderStatus(orderStatus);

        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
