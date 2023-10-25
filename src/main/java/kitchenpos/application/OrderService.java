package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderLineItemCreateRequest;
import kitchenpos.ui.request.OrderUpdateRequest;
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
        List<OrderLineItem> orderLineItems = createOrderLineItemsByRequest(request.getOrderLineItemCreateRequests());
        OrderTable orderTable = findOrderTable(request.getOrderTableId());

        Order order = Order.of(orderLineItems);
        orderTable.addOrder(order);

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

        return OrderLineItem.of(menu, orderLineItemCreateRequest.getQuantity());
    }

    private Menu findMenu(Long menuId) {
        validateMenuId(menuId);

        return menuRepository.findById(menuId)
                .orElseThrow(IllegalArgumentException::new);
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
