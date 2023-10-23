package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Orders;
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
    public Orders create(OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = createOrderLineItemsByRequest(request.getOrderLineItemCreateRequests());
        OrderTable orderTable = findOrderTable(request.getOrderTableId());

        Orders orders = Orders.of(COOKING, orderLineItems);
        orderTable.addOrder(orders);

        return orderRepository.save(orders);
    }

    private List<OrderLineItem> createOrderLineItemsByRequest(List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        if (CollectionUtils.isEmpty(orderLineItemCreateRequests)) {
            throw new IllegalArgumentException();
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
        validateNull(menuId);

        return menuRepository.findById(menuId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateNull(Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        validateNull(orderTableId);

        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Orders> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Orders changeOrderStatus(Long orderId, OrderUpdateRequest request) {
        OrderStatus orderStatus = OrderStatus.from(request.getOrderStatus());
        Orders orders = findOrder(orderId);
        orders.changeOrderStatus(orderStatus);

        return orders;
    }

    private Orders findOrder(Long orderId) {
        validateNull(orderId);

        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }

}
