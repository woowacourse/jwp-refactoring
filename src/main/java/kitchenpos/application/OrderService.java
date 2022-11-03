package kitchenpos.application;

import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.OrderChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderMenu;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderMenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;
    private final OrderMenuRepository orderMenuRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final MenuRepository menuRepository,
            final OrderMenuRepository orderMenuRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
        this.orderMenuRepository = orderMenuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 테이블입니다. [%s]", request.getOrderTableId())));
        validateOrderTableIsNotEmpty(orderTable);

        final Order order = toOrder(request);

        final Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
            .map(OrderResponse::new)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeRequest request) {
        final Order changedOrder = changeStatus(orderId, OrderStatus.valueOf(request.getOrderStatus()));
        return new OrderResponse(changedOrder);
    }

    private void validateOrderTableIsNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있으면 안됩니다.");
        }
    }

    public Order toOrder(final OrderRequest request) {
        return new Order(
            request.getOrderTableId(),
            OrderStatus.COOKING,
            LocalDateTime.now(),
            toOrderLineItems(request.getOrderLineItems())
        );
    }

    public List<OrderLineItem> toOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
            .map(request -> {
                final Menu menu = findMenuById(request);
                final OrderMenu orderMenu = new OrderMenu(menu.getName(), menu.getPrice());
                return new OrderLineItem(orderMenu, request.getQuantity());
            })
            .collect(Collectors.toUnmodifiableList());
    }

    private Menu findMenuById(final OrderLineItemRequest request) {
        return menuRepository.findById(request.getMenuId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다."));
    }

    private Order changeStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 주문입니다. [%s]", orderId)));

        savedOrder.changeStatus(orderStatus);
        return orderRepository.findById(savedOrder.getId())
            .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 주문입니다. [%s]", savedOrder.getId())));
    }
}
