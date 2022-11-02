package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderCreationDto;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.application.dto.OrderLineItemCreationDto;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.OrderedMenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderedMenu;
import kitchenpos.domain.product.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;
    private final OrderedMenuDao orderedMenuDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao,
            final OrderedMenuDao orderedMenuDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
        this.orderedMenuDao = orderedMenuDao;
    }

    @Transactional
    public OrderDto create(final OrderCreationDto orderCreationDto) {
        List<OrderLineItem> orderLineItems = createOrderedMenuAndGetOrderLineItems(orderCreationDto);
        final Order order = OrderCreationDto.toEntity(orderCreationDto);
        validateOrderTable(order);

        final Order savedOrder = orderDao.save(
                new Order(order.getOrderTableId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = orderLineItems.stream()
                .map(orderLineItem -> orderLineItemDao.save(
                        new OrderLineItem(orderId, orderLineItem.getOrderedMenuId(), orderLineItem.getQuantity())))
                .collect(Collectors.toList());

        return OrderDto.from(savedOrder.saveOrderLineItems(savedOrderLineItems));
    }

    private List<OrderLineItem> createOrderedMenuAndGetOrderLineItems(final OrderCreationDto orderCreationDto) {
        return orderCreationDto.getOrderLineItems()
                .stream()
                .map(orderLineItemDto -> {
                    final Menu menu = menuDao.findById(orderLineItemDto.getMenuId())
                            .orElseThrow(IllegalArgumentException::new);
                    final OrderedMenu orderedMenu = orderedMenuDao.save(new OrderedMenu(menu.getName(), new Price(menu.getPrice())));
                    return new OrderLineItem(orderedMenu.getId(), orderLineItemDto.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private void validateOrderTable(final Order order) {
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

        validateOrderStatus(savedOrder);

        final Order order = orderDao.save(new Order(savedOrder.getId(),
                savedOrder.getOrderTableId(),
                OrderStatus.valueOf(orderStatus),
                savedOrder.getOrderedTime(),
                orderLineItemDao.findAllByOrderId(orderId)));

        return OrderDto.from(order);
    }

    private void validateOrderStatus(final Order order) {
        if (order.isInCompletionStatus()) {
            throw new IllegalArgumentException();
        }
    }
}
