package kitchenpos.application;

import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemCreateRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.application.dto.OrderStatusDto;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderTable orderTable = getOrderTable(request);
        validateEmptyOrderTable(orderTable);

        final Order order = createOrderRequest(orderTable.getId(), request.getOrderLineItems());
        validateSameCountAsItemAndMenu(request.getOrderLineItems(), order);

        return OrderResponse.from(orderDao.save(order));
    }

    private OrderTable getOrderTable(final OrderCreateRequest request) {
        return orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 없습니다."));
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비었습니다.");
        }
    }

    private static Order createOrderRequest(final Long orderTableId, final List<OrderLineItemCreateRequest> orderLineItems) {
        return new Order(orderTableId,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderLineItems.stream()
                        .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                        .collect(Collectors.toList()));
    }

    private void validateSameCountAsItemAndMenu(final List<OrderLineItemCreateRequest> orderLineItems, final Order order) {
        final int countOfMenuIds = getCountOfMenuIds(orderLineItems);
        if (!order.isValidMenuSize(countOfMenuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private int getCountOfMenuIds(final List<OrderLineItemCreateRequest> orderLineItems) {
        return (int) orderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .count();
    }

    public List<OrderResponse> list() {
        return orderDao.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderStatusDto changeOrderStatus(final Long orderId, final OrderStatusDto request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return new OrderStatusDto(savedOrder.getOrderStatus());
    }
}
