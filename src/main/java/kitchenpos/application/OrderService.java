package kitchenpos.application;

import kitchenpos.application.dto.convertor.OrderConvertor;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.OrderChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
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
    public OrderResponse create(final OrderRequest request) {
        final Order order = OrderConvertor.convertToOrder(request);

        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }

        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 테이블입니다. [%s]", order.getOrderTableId())));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있으면 안됩니다.");
        }

        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return OrderConvertor.convertToOrderResponse(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return OrderConvertor.convertToOrderResponse(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeRequest request) {
        final Order order = OrderConvertor.convertToOrder(request);

        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 주문입니다. [%s]", orderId)));
        savedOrder.changeStatus(order.getOrderStatus());

        orderDao.save(savedOrder);
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return OrderConvertor.convertToOrderResponse(savedOrder);
    }
}
