package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderCreateRequest.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderValidator;
import kitchenpos.domain.order.order_lineitem.MenuInfoGenerator;
import kitchenpos.domain.order.order_lineitem.OrderLineItem;
import kitchenpos.domain.order.order_lineitem.OrderLineItems;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repositroy.OrderRepository;
import kitchenpos.support.AggregateReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuInfoGenerator menuInfoGenerator;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final MenuInfoGenerator menuInfoGenerator,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.menuInfoGenerator = menuInfoGenerator;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final AggregateReference<OrderTable> orderTableId = new AggregateReference<>(request.getOrderTableId());
        final OrderLineItems orderLineItems = toOrderLineItems(request.getOrderLineItems());
        final Order order = new Order(orderTableId, orderLineItems, orderValidator, LocalDateTime.now());

        return OrderResponse.from(orderRepository.save(order));
    }

    private OrderLineItems toOrderLineItems(final List<OrderLineItemRequest> requests) {
        return new OrderLineItems(requests.stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toUnmodifiableList())
        );
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest request) {
        final AggregateReference<Menu> menuId = new AggregateReference<>(request.getMenuId());
        return new OrderLineItem(menuId, request.getQuantity(), menuInfoGenerator);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAllByFetch()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order order = orderRepository.getById(orderId);
        order.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(order);
    }
}
