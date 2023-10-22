package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusChangeRequest;
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
            throw new IllegalArgumentException();
        }
        final List<Menu> menus = menuRepository.findAllById(menuIds);
        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable validateOrderTableIsEmpty(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
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
            .orElseThrow(IllegalArgumentException::new);
        order.changeStatus(request.getStatus());

        return OrderResponse.from(order);
    }
}
