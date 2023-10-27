package kitchenpos.application;

import kitchenpos.domain.dto.OrderRequest;
import kitchenpos.domain.dto.OrderResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
    public OrderResponse create(final OrderRequest orderRequest) {
        validateOrderTable(orderRequest);
        validateOrderLineItems(orderRequest);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        validateOrderTableOrderable(orderTable);

        final OrderLineItems orderLineItems = mapToOrderLineItems(orderRequest);
        final Order order = new Order(orderTable, orderLineItems);
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    private void validateOrderTableOrderable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있는 상태입니다.");
        }
    }

    private OrderLineItems mapToOrderLineItems(final OrderRequest orderRequest) {
        final List<OrderRequest.OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();

        final List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                    final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                            .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));

                    return new OrderLineItem(
                            menu.getId(),
                            orderLineItemRequest.getQuantity(),
                            menu.getName(),
                            menu.getPrice()
                    );
                })
                .collect(Collectors.toList());

        return new OrderLineItems(orderLineItems);
    }

    private void validateOrderTable(final OrderRequest orderRequest) {
        if (Objects.isNull(orderRequest.getOrderTableId())) {
            throw new IllegalArgumentException("주문 테이블의 ID가 존재하지 않습니다.");
        }
    }

    private void validateOrderLineItems(final OrderRequest orderRequest) {
        if (orderRequest.getOrderLineItems().isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 비어있습니다.");
        }

        final List<Long> menuIds = orderRequest.getOrderLineItems().stream()
                .map(OrderRequest.OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 존재합니다.");
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        validateOrderId(orderId);

        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        order.updateOrderStatus(orderRequest.getOrderStatus());

        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    private void validateOrderId(final Long orderId) {
        if (Objects.isNull(orderId)) {
            throw new IllegalArgumentException("주문 ID가 존재하지 않습니다.");
        }
    }
}
