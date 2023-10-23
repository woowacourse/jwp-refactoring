package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.order.MenuQuantityDto;
import kitchenpos.application.dto.order.OrderRequest;
import kitchenpos.application.dto.order.OrderResponse;
import kitchenpos.application.dto.order.OrderStatusChangeRequest;
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
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = convertToOrderLineItems(orderRequest.getOrderLineItems());

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        // TODO: 2023-10-23 request 받아오도록 리팩터링
        final Order orderToSave = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        final Order savedOrder = orderDao.save(orderToSave);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            final OrderLineItem orderLineItemToSave = new OrderLineItem(orderId, orderLineItem.getMenuId(), orderLineItem.getQuantity());
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItemToSave));
        }
        savedOrder.addOrderLineItems(savedOrderLineItems);

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> convertToOrderLineItems(final List<MenuQuantityDto> menuQuantities) {
        return menuQuantities
            .stream()
            .map(menuIdWithQuantity ->
                new OrderLineItem(null, menuIdWithQuantity.getMenuId(), menuIdWithQuantity.getQuantity())
            )
            .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.addOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest changeRequest) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(changeRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        orderDao.save(savedOrder);

        savedOrder.addOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return OrderResponse.from(savedOrder);
    }
}
