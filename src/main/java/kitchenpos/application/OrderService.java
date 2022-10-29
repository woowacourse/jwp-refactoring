package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderCreationDto;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    public OrderDto create(final OrderCreationDto orderCreationDto) {
        final Order order = OrderCreationDto.toEntity(orderCreationDto);
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        validateOrderCondition(order);

        final Order savedOrder = orderDao.save(
                new Order(order.getOrderTableId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = orderLineItems.stream()
                .map(orderLineItem -> orderLineItemDao.save(
                        new OrderLineItem(orderId, orderLineItem.getMenuId(), orderLineItem.getQuantity())))
                .collect(Collectors.toList());

        return OrderDto.from(savedOrder.saveOrderLineItems(savedOrderLineItems));
    }

    private void validateOrderCondition(final Order order) {
        final List<Long> menuIds = order.getOrderLineItemsMenuId();
        if (menuIds.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }


    @Transactional(readOnly = true)
    public List<OrderDto> getOrders() {
        return orderDao.findAll()
                .stream()
                .map(order -> order.saveOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId())))
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrder.isInCompletionStatus()) {
            throw new IllegalArgumentException();
        }

        final Order order = orderDao.save(new Order(savedOrder.getId(),
                savedOrder.getOrderTableId(),
                OrderStatus.valueOf(orderStatus),
                savedOrder.getOrderedTime(),
                orderLineItemDao.findAllByOrderId(orderId)));

        return OrderDto.from(order);
    }
}
