package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderCreateRequest.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repositroy.MenuRepository;
import kitchenpos.repositroy.OrderRepository;
import kitchenpos.repositroy.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            final OrderTableRepository orderTableRepository,
            final MenuRepository menuRepository,
            final OrderRepository orderRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());
        final OrderLineItems orderLineItems = toOrderLineItems(request.getOrderLineItems());
        return OrderResponse.from(orderRepository.save(new Order(orderTable, orderLineItems)));
    }

    private OrderLineItems toOrderLineItems(final List<OrderLineItemRequest> requests) {
        return new OrderLineItems(requests.stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toUnmodifiableList())
        );
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest request) {
        final Menu menu = menuRepository.getById(request.getMenuId());
        return new OrderLineItem(menu, request.getQuantity());
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
