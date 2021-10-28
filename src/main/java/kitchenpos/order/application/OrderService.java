package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequestDto;
import kitchenpos.order.dto.OrderCreateResponseDto;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.table.repository.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
        final MenuDao menuDao,
        final OrderDao orderDao,
        final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderCreateResponseDto create(final OrderCreateRequestDto orderCreateRequestDto) {
        List<OrderLineItem> orderLineItemGroup = new ArrayList<>();
        for (OrderLineItemDto orderLineItemDto : orderCreateRequestDto.getOrderLineItems()) {
            orderLineItemGroup.add(new OrderLineItem(orderLineItemDto.getMenuId(), orderLineItemDto.getQuantity()));
        }
        Order order = new Order(
            orderCreateRequestDto.getOrderTableId(),
            orderCreateRequestDto.getOrderStatus(),
            orderCreateRequestDto.getOrderedTime(),
            orderLineItemGroup);
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        order.shouldNotEmpty(orderLineItems);

        final List<Long> menuIds = order.getMenuIds();

        shouldExistingLineItems(orderLineItems, menuIds);
        shouldNotOrderTableEmpty(order);

        Order savedOrder = orderDao.save(order);
        return new OrderCreateResponseDto(savedOrder);
    }

    private void shouldNotOrderTableEmpty(Order order) {
        OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void shouldExistingLineItems(List<OrderLineItem> orderLineItems, List<Long> menuIds) {
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();
        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);
        return savedOrder;
    }
}
