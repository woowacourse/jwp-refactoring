package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderUpdateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final Order order = request.toOrder();
        final long menuCount = menuRepository.countByIdIn(order.getMenuIds());
        if (!order.hasValidSize(menuCount)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = getOrderTableById(order.getOrderTableId());

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    private OrderTable getOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
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

        final OrderStatus orderStatus = OrderStatus.from(request.getOrderStatus());
        final Order changedOrder = savedOrder.updateOrderStatus(orderStatus.name());

        final Order order = orderRepository.save(changedOrder);
        return OrderResponse.from(order);
    }
}
