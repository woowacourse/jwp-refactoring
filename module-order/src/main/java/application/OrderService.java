package application;

import application.dto.OrderCreateRequest;
import application.dto.OrderCreateRequest.OrderLineItemRequest;
import application.dto.OrderResponse;
import application.dto.OrderStatusChangeRequest;
import domain.Menu;
import domain.Order;
import domain.OrderStatus;
import domain.OrderTable;
import domain.OrderValidator;
import java.time.LocalDateTime;
import java.util.List;
import domain.order_lineitem.MenuInfoGenerator;
import domain.order_lineitem.OrderLineItem;
import domain.order_lineitem.OrderLineItems;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.OrderRepository;
import support.AggregateReference;

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
