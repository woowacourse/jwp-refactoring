package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public OrderDto create(final OrderDto orderDto) {
        final List<OrderLineItemDto> orderLineItemDtos = orderDto.getOrderLineItems();
        validateOrderLineRequest(orderLineItemDtos);

        final OrderTable orderTable = getFilledOrderTable(orderDto);
        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(),
                LocalDateTime.now());

        Order savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            OrderLineItem orderLineItem = new OrderLineItem(orderId, orderLineItemDto.getMenuId(),
                    orderLineItemDto.getQuantity());
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder = new Order(savedOrder.getId(), savedOrder.getOrderTableId(),
                savedOrder.getOrderStatus(), savedOrder.getOrderedTime(), savedOrderLineItems);

        return OrderDto.from(savedOrder);
    }

    private void validateOrderLineRequest(List<OrderLineItemDto> orderLineItemDtos) {
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

    private OrderTable getFilledOrderTable(OrderDto orderDto) {
        final OrderTable orderTable = orderTableDao.findById(orderDto.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orderTable;
    }

    public List<OrderDto> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(order -> new Order(order.getId(), order.getOrderTableId(),
                        order.getOrderStatus(), order.getOrderedTime(),
                        orderLineItemDao.findAllByOrderId(order.getId())))
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderDto orderDto) {
        Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrder.isOrderCompletion()) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orderDto.getOrderStatus());
        savedOrder = new Order(savedOrder.getId(), savedOrder.getOrderTableId(),
                orderStatus.name(), savedOrder.getOrderedTime());
        orderDao.save(savedOrder);

        savedOrder = new Order(savedOrder.getId(), savedOrder.getOrderTableId(),
                orderStatus.name(), savedOrder.getOrderedTime(),
                orderLineItemDao.findAllByOrderId(orderId));
        return OrderDto.from(savedOrder);
    }
}
