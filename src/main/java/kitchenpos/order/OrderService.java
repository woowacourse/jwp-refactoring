package kitchenpos.order;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuRepository;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderCreateRequest.OrderLineItemCreateRequest;
import kitchenpos.order.dto.OrderCreateResponse;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public OrderCreateResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemCreateRequest> requestOrderLineItems = request.getOrderLineItems();
        checkValidOrderLineItems(requestOrderLineItems);

        final OrderTable orderTable = findOrderTableById(request.getOrderTableId());
        final List<OrderLineItem> orderLineItems = makeOrderLineItems(requestOrderLineItems);

        final Order order = new Order(orderTable, OrderStatus.COOKING, orderLineItems);
        final Order savedOrder = orderRepository.save(order);

        return OrderCreateResponse.of(savedOrder);
    }

    private void checkValidOrderLineItems(final List<OrderLineItemCreateRequest> requestOrderLineItems) {
        final List<Long> menuIds = requestOrderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(toList());

        if (requestOrderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("메뉴 정보가 올바르지 않습니다.");
        }
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다. id = " + orderTableId));
    }

    private List<OrderLineItem> makeOrderLineItems(final List<OrderLineItemCreateRequest> requestOrderLineItems) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest requestOrderLineItem : requestOrderLineItems) {
            final Menu menu = findMenuById(requestOrderLineItem.getMenuId());
            final OrderLineItem orderLineItem = new OrderLineItem(menu, requestOrderLineItem.getQuantity());
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    private Menu findMenuById(final Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다. id = " + menuId));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = findOrderById(orderId);

        checkCompleted(savedOrder);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus().name());
        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.of(savedOrder);
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다. id = " + orderId));
    }

    private void checkCompleted(final Order savedOrder) {
        if (savedOrder.completed()) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }
    }
}
