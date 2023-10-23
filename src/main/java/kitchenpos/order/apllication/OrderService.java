package kitchenpos.order.apllication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.vo.OrderStatus;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long create(final OrderCreateRequest request) {
        final Order order = new Order(
                orderTableRepository.getById(request.orderTableId()),
                OrderStatus.COOKING,
                LocalDateTime.now()
        );

        final List<OrderLineItem> orderLineItems = createOrderLineItems(request);
        order.addOrderLineItems(orderLineItems);

        return orderRepository.save(order).id();
    }

    private List<OrderLineItem> createOrderLineItems(final OrderCreateRequest request) {
        final List<OrderLineItemCreateRequest> orderLineItemCreateRequests = request.orderLineItems();

        final List<Long> menuIds = orderLineItemCreateRequests.stream()
                .map(OrderLineItemCreateRequest::menuId)
                .collect(Collectors.toUnmodifiableList());
        final List<Menu> menus = menuRepository.getAllById(menuIds);

        final List<Long> quantities = orderLineItemCreateRequests.stream()
                .map(OrderLineItemCreateRequest::quantity)
                .collect(Collectors.toUnmodifiableList());

        return IntStream.range(0, menus.size())
                .mapToObj(index -> new OrderLineItem(menus.get(index), quantities.get(index)))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus requestOrderStatus) {
        final Order savedOrder = orderRepository.getById(orderId);
        savedOrder.changeOrderStatus(requestOrderStatus);
        return OrderResponse.from(savedOrder);
    }
}
