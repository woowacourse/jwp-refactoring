package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusChangeRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderLineItemRepository orderLineItemRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        validateOrderLineItem(orderRequest);
        final OrderTable orderTable = validateOrderTableIsEmpty(orderRequest);
        final Order order = new Order(orderTable.getId());
        orderRepository.save(order);
        addOrderLineItems(getForSaveOrderLineItems(orderRequest.getOrderLineItems()), order);
        return OrderResponse.from(order);
    }

    private void validateOrderLineItem(final OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getMenuId();
        if (menuIds.isEmpty()) {
            throw new IllegalArgumentException("메뉴없이 주문은 할 수 없습니다.");
        }
        final List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException("존재하지 않은 메뉴로 주문을 할 수 없습니다.");
        }
    }

    private OrderTable validateOrderTableIsEmpty(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 현재 비어있습니다.");
        }
        return orderTable;
    }

    private List<OrderLineItem> getForSaveOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .map(orderLineItemRequest -> new OrderLineItem(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
            .collect(Collectors.toUnmodifiableList());
    }

    private void addOrderLineItems(final List<OrderLineItem> orderLineItems, final Order order) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            order.addOrderLineItems(orderLineItem);
            orderLineItemRepository.save(orderLineItem);
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        order.changeStatus(request.getStatus());

        return OrderResponse.from(order);
    }
}
