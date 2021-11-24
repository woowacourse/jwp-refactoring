package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = orderRequest.toOrder();
        validateOrderTable(order);

        order.startCooking();
        orderRepository.save(order);
        updateOrderLineItems(order);

        return OrderResponse.of(order);
    }

    private void validateOrderTable(Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        orderTable.validateNotEmpty();
    }

    private void updateOrderLineItems(Order order) {
        final OrderLineItems orderLineItems = order.getOrderLineItems();
        final List<Long> menuIds = orderLineItems.getMenuIds();

        orderLineItems.validateSameSize(menuRepository.countByIdIn(menuIds));
        orderLineItems.updateOrderInfo(order);
        orderLineItemRepository.saveAll(orderLineItems.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        final OrderStatus orderStatus = orderRequest.getOrderStatus();

        order.changeOrderStatus(orderStatus);
        return OrderResponse.of(order);
    }
}
