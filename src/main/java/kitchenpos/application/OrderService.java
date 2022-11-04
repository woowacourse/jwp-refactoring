package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderStatusDto;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemCreateRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuDao menuRepository;
    private final OrderDao orderRepository;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuRepository,
            final OrderDao orderRepository,
            final OrderTableDao orderTableDao
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderTable orderTable = getOrderTable(request);
        validateEmptyOrderTable(orderTable);

        final Order order = createOrderRequest(orderTable.getId(), request.getOrderLineItems());
        validateSameCountAsItemAndMenu(request.toOrderLineItems(), order);

        return OrderResponse.from(orderRepository.save(order));
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

    private static Order createOrderRequest(final Long orderTableId,
                                            final List<OrderLineItemCreateRequest> orderLineItems) {
        return new Order(orderTableId,
                OrderStatus.COOKING,
                LocalDateTime.now(),
                orderLineItems.stream()
                        .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                        .collect(Collectors.toList()));
    }

    private void validateSameCountAsItemAndMenu(final List<OrderLineItem> orderLineItems,
                                                final Order order) {
        final List<Long> menuIds = getCountOfMenuIds(orderLineItems);
        final long countByIdIn = menuRepository.countByIdIn(menuIds);
        if (!order.isValidMenuSize(countByIdIn)) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> getCountOfMenuIds(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderStatusDto changeOrderStatus(final Long orderId, final OrderStatusDto request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return new OrderStatusDto(savedOrder.getOrderStatus().name());
    }
}
