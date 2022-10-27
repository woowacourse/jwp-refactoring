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
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.request.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
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
    public Order create(OrderCreateRequest orderCreateRequest) {
        List<Long> menuIds = getMenuIds(orderCreateRequest.getOrderLineItems());
        validateOrderLineItems(menuIds);
        validateOrderTable(orderCreateRequest.getOrderTableId());

        Order order = new Order(orderCreateRequest.getOrderTableId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        Order savedOrder = orderDao.save(order);

        Long orderId = savedOrder.getId();
        List<OrderLineItem> savedOrderLineItems = getOrderLineItems(orderCreateRequest.getOrderLineItems(), orderId);

        return new Order(savedOrder.getId(), savedOrder.getOrderTableId(), savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(), savedOrderLineItems);
    }

    private List<Long> getMenuIds(List<OrderLineItemDto> orderLineItemDtos) {
        return orderLineItemDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItems(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)
                || (menuIds.size() != menuDao.countByIdIn(menuIds))) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> getOrderLineItems(List<OrderLineItemDto> orderLineItemDtos, Long orderId) {
        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            OrderLineItem orderLineItem = new OrderLineItem(orderId, orderLineItemDto.getMenuId(),
                    orderLineItemDto.getQuantity());
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return savedOrderLineItems;
    }

    public List<Order> list() {
        List<Order> orders = orderDao.findAll();
        List<Order> newOrders = new ArrayList<>();

        for (Order order : orders) {
            List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());
            newOrders.add(new Order(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
                    order.getOrderedTime(), orderLineItems));
        }

        return newOrders;
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, ChangeOrderStatusRequest changeOrderStatusRequest) {
        Order savedOrder = findOrder(orderId);
        validateOrderStatus(savedOrder);

        Order order = new Order(
                savedOrder.getId(),
                savedOrder.getOrderTableId(),
                changeOrderStatusRequest.getOrderStatus(),
                savedOrder.getOrderedTime(),
                orderLineItemDao.findAllByOrderId(orderId));

        return orderDao.save(order);
    }

    private Order findOrder(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderStatus(Order savedOrder) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }
}
