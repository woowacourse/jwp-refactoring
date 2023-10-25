package kitchenpos.order.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.OrderUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = createOrderLineItemsByRequest(
                request.getOrderLineItemCreateRequests()
        );
        OrderTable orderTable = findOrderTable(request.getOrderTableId());

        validateOrderTableEmpty(orderTable);

        Order order = Order.of(orderTable.getId(), orderLineItems);

        return orderRepository.save(order);
    }

    private List<OrderLineItem> createOrderLineItemsByRequest(List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        if (CollectionUtils.isEmpty(orderLineItemCreateRequests)) {
            throw new IllegalArgumentException("주문 항목은 1개 이상이어야 합니다.");
        }

        return orderLineItemCreateRequests.stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(OrderLineItemCreateRequest orderLineItemCreateRequest) {
        Menu menu = findMenu(orderLineItemCreateRequest.getMenuId());

        return OrderLineItem.of(
                menu.getName(),
                menu.getPrice(),
                menu.getId(),
                orderLineItemCreateRequest.getQuantity()
        );
    }

    private Menu findMenu(Long menuId) {
        validateMenuId(menuId);

        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴는 존재하지 않을 수 없습니다."));
    }

    private void validateMenuId(Long menuId) {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException("메뉴의 ID 는 존재하지 않을 수 없습니다.");
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        validateOrderTableId(orderTableId);

        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderTableId(Long orderTableId) {
        if (Objects.isNull(orderTableId)) {
            throw new IllegalArgumentException("주문 테이블의 ID 는 존재하지 않을 수 없습니다.");
        }
    }

    private void validateOrderTableEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 주문 불가 상태입니다.");
        }
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderUpdateRequest request) {
        OrderStatus orderStatus = OrderStatus.from(request.getOrderStatus());
        Order order = findOrder(orderId);
        order.changeOrderStatus(orderStatus);

        return order;
    }

    private Order findOrder(Long orderId) {
        validateOrderId(orderId);

        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderId(Long orderId) {
        if (Objects.isNull(orderId)) {
            throw new IllegalArgumentException("주문의 ID 는 존재하지 않을 수 없습니다.");
        }
    }

}
