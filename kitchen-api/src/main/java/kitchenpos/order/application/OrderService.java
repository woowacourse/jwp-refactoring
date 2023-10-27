package kitchenpos.order.application;

import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.application.dto.request.OrderCreateRequest;
import kitchenpos.order.application.dto.request.OrderStatusModifyRequest;
import kitchenpos.order.application.dto.response.OrderQueryResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public OrderQueryResponse create(final OrderCreateRequest request) {
        final OrderLineItems orderLineItems = request.toOrder().getOrderLineItems();
        validateMenus(orderLineItems);
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        final Order order = Order.of(orderTable, orderLineItems);

        return OrderQueryResponse.from(orderRepository.save(order));
    }

    private void validateMenus(final OrderLineItems orderLineItems) {
        final List<Long> menuIds = orderLineItems.extractMenuIds();
        if (orderLineItems.isDifferentSize(menuRepository.countByIdIn(menuIds))) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderQueryResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderQueryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderQueryResponse changeOrderStatus(final Long orderId,
                                                final OrderStatusModifyRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        final OrderStatus orderStatus = request.getOrderStatus();

        savedOrder.updateOrderStatus(orderStatus);

        return OrderQueryResponse.from(orderRepository.save(savedOrder));
    }
}
