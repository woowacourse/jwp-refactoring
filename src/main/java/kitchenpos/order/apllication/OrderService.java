package kitchenpos.order.apllication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.vo.OrderStatus;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final OrderLineItemRepository orderLineItemRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public Long create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.orderTableId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "[ERROR] OrderTable이 존재하지 않습니다. id : " + request.orderTableId()
                ));

        final Order order = new Order(
                OrderStatus.COOKING,
                LocalDateTime.now()
        );
        order.setOrderTable(orderTable);
        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItem> orderLineItems = createOrderLineItems(request);
        orderLineItems.forEach(orderLineItem -> {
            orderLineItem.setOrder(savedOrder);
            orderLineItemRepository.save(orderLineItem);
        });

        return savedOrder.id();
    }

    private List<OrderLineItem> createOrderLineItems(final OrderCreateRequest request) {
        final List<OrderLineItemCreateRequest> orderLineItemCreateRequests = request.orderLineItems();

        final List<Long> menuIds = orderLineItemCreateRequests.stream()
                .map(OrderLineItemCreateRequest::menuId)
                .collect(Collectors.toUnmodifiableList());
        final List<Menu> menus = menuRepository.findAllById(menuIds);

        if (orderLineItemCreateRequests.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] OrderLineItem이 비어있습니다.");
        }

        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException("[ERROR] 없는 메뉴가 존재합니다.");
        }

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
                .map(this::getOrderResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus requestOrderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] Order가 존재하지 않습니다. id : " + orderId));
        savedOrder.changeOrderStatus(requestOrderStatus);

        return getOrderResponse(savedOrder);
    }

    private OrderResponse getOrderResponse(final Order order) {
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findByOrderId(order.id());
        return OrderResponse.of(order, orderLineItems);
    }
}
