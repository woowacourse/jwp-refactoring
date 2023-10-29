package kitchenpos.application;

import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderStatusChangeRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
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
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
        final Order order = new Order(orderTable.getId());
        final OrderLineItems orderLineItems = getOrderLineItems(orderRequest.getOrderLineItems(), order);
        orderRepository.save(order);
        return OrderResponse.from(order, orderLineItems);
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

    private OrderLineItems getOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests,
                                             final Order order) {
        final List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
            .map(orderLineItemRequest -> {
                final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
                return new OrderLineItem(order,
                    orderLineItemRequest.getMenuId(),
                    orderLineItemRequest.getQuantity(),
                    menu.getName(), menu.getPrice().intValue());
            })
            .collect(Collectors.toUnmodifiableList());
        return new OrderLineItems(orderLineItems);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
            .map(order -> {
                final List<OrderLineItem> orderLineItems = orderLineItemRepository.findByOrder(order);
                return OrderResponse.from(order, new OrderLineItems(orderLineItems));
            })
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        order.changeStatus(request.getStatus());
        final OrderLineItems orderLineItems = new OrderLineItems(orderLineItemRepository.findByOrder(order));
        return OrderResponse.from(order, orderLineItems);
    }
}
