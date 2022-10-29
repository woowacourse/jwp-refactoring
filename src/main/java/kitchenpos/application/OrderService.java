package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderUpdateRequest;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderRepository orderRepository;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderRepository orderRepository,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderRepository = orderRepository;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final Order order = request.toOrder();
        checkExistMenuIn(order);
        final OrderTable orderTable = getOrderTable(order);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    private void checkExistMenuIn(Order order) {
        final long menuCount = menuDao.countByIdIn(order.getMenuIds());
        if (!order.hasValidSize(menuCount)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTable(Order order) {
        return orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrder.hasStatus(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.from(request.getOrderStatus());
        savedOrder.updateOrderStatus(orderStatus.name());

        final Order order = orderRepository.save(savedOrder);
        return OrderResponse.from(order);
    }
}
