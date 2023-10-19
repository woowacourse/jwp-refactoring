package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderlineitem.OrderLineItem;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    public Order create(final OrderRequest request) {
        final List<OrderLineItemDto> orderLineItemsDtos = request.getOrderLineItems();
        validateExistenceOfOrderLineItem(orderLineItemsDtos);
        final OrderTable orderTable = findOrderTable(request);
        final Order savedOrder = orderDao.save(new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now()));
        saveOrderLineItems(orderLineItemsDtos, savedOrder);

        return savedOrder;
    }

    private OrderTable findOrderTable(final OrderRequest request) {
        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return orderTable;
    }

    private void validateExistenceOfOrderLineItem(final List<OrderLineItemDto> orderLineItemsDtos) {
        final List<Long> menuIds = orderLineItemsDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemsDtos.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void saveOrderLineItems(final List<OrderLineItemDto> orderLineItemsDtos, final Order savedOrder) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemDto orderLineItemDto : orderLineItemsDtos) {
            final OrderLineItem orderLineItem = new OrderLineItem(
                    savedOrder.getId(),
                    orderLineItemDto.getMenuId(),
                    new Quantity(orderLineItemDto.getQuantity())
            );
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.updateOrderLineItems(savedOrderLineItems);
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.updateOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order findOrder = findOrder(orderId);
        findOrder.updateOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        orderDao.save(findOrder);
        findOrder.updateOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return findOrder;
    }

    private Order findOrder(final Long orderId) {
        final Order findOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        if (Objects.equals(OrderStatus.COMPLETION, findOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        return findOrder;
    }
}
