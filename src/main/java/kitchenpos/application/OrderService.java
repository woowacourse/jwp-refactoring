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
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
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
        final List<OrderLineItem> orderLineItems = toOrderLineItems(orderRequest);

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("하나 이상의 메뉴를 주문해야 한다.");
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        long countByIdIn = menuDao.countByIdIn(menuIds);
        if (orderLineItems.size() != countByIdIn) {
            throw new IllegalArgumentException("주문한 메뉴들은 모두 DB에 등록되어야 한다.");
        }

        final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블은 DB에 등록되어야 한다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블은 손님이 존재해야 한다.");
        }

        Order entity = new Order(null,
                orderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderLineItems);

        final Order savedOrder = orderDao.save(entity);

        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.updateOrderLineItems(savedOrderLineItems);

        return toOrderResponse(savedOrder);
    }

    private OrderResponse toOrderResponse(Order savedOrder) {
        return new OrderResponse(savedOrder.getId(), savedOrder.getOrderTableId(), savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(), toOrderLineItemResponses(savedOrder));
    }

    private List<OrderLineItem> toOrderLineItems(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems().stream()
                .map(itemRequest -> new OrderLineItem(null, null, itemRequest.getMenuId(), itemRequest.getQuantity()))
                .collect(
                        Collectors.toList());
    }

    private List<OrderLineItemResponse> toOrderLineItemResponses(Order savedOrder) {
        return savedOrder.getOrderLineItems().stream()
                .map(orderLineItem -> new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity())).collect(
                        Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.updateOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest statusRequest) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 주문은 존재하지 않는다."));

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException("주문 상태가 COMPLETION일 시 주문 상태를 변경할 수 없다.");
        }

        final OrderStatus orderStatus = statusRequest.getOrderStatus();
        savedOrder.updateOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.updateOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return toOrderResponse(savedOrder);
    }
}
