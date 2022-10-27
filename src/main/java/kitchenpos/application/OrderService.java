package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.OrderCreateRequest;
import kitchenpos.application.request.OrderLineItemRequest;
import kitchenpos.application.request.OrderUpdateRequest;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableDao orderTableDao
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 OrderTable 입니다."));
        validateOrderTableEmpty(orderTable);

        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING);
        final OrderLineItems orderLineItems = createOrderLineItems(request, order);
        order.updateOrderLineItems(orderLineItems);
        return orderRepository.save(order);
    }

    private void validateOrderTableEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalStateException("해당 OrderTable이 empty 상태 입니다.");
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Order 입니다."));

        savedOrder.changeStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return savedOrder;
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest request, Order order) {
        final Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴가 존재하지 않습니다."));
        return new OrderLineItem(order, menu.getId(), request.getQuantity());
    }

    private OrderLineItems createOrderLineItems(final OrderCreateRequest request, final Order order) {
        return new OrderLineItems(request.getOrderLineItems()
                .stream()
                .map(it -> toOrderLineItem(it, order))
                .collect(Collectors.toList()));
    }
}
