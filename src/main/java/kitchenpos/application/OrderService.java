package kitchenpos.application;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kitchenpos.application.request.OrderLineItemRequest;
import kitchenpos.application.request.OrderRequest;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
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
    public OrderResponse create(final OrderRequest request) {
        validateSavedMenuSize(request);
        validateOrderTableNotEmpty(request);

        final Order order = Order.of(request.getOrderTableId(), LocalDateTime.now(),
                getOrderLineItems(request));
        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.from(savedOrder);
    }

    private void validateSavedMenuSize(final OrderRequest request) {
        final List<Long> menuIds = request.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(toList());

        if (request.getOrderLineItems().size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문한 메뉴 항목 개수와 실제 메뉴의 수가 일치하지 않습니다.");
        }
    }

    private void validateOrderTableNotEmpty(final OrderRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있으면 주문을 생성할 수 없다.");
        }
    }

    private List<OrderLineItem> getOrderLineItems(final OrderRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        final Map<Long, Long> menuAndQuantity = orderLineItemRequests.stream()
                .collect(toMap(OrderLineItemRequest::getMenuId, OrderLineItemRequest::getQuantity));

        final List<Menu> menus = menuRepository.findAllById(menuAndQuantity.keySet());

        return getOrderLineItems(menuAndQuantity, menus);
    }

    private static List<OrderLineItem> getOrderLineItems(final Map<Long, Long> menuAndQuantity, final List<Menu> menus) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Menu menu : menus) {
            final OrderLineItem orderLineItem = new OrderLineItem(menu, menuAndQuantity.get(menu.getId()));
            orderLineItems.add(orderLineItem);
        }

        return orderLineItems;
    }
}
