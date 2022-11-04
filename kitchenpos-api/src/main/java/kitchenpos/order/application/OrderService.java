package kitchenpos.order.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.dto.OrderChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineItemSaveRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderSaveRequest;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderSaveRequest request) {
        validateEmptyOrderTable(request.getOrderTableId());
        Order savedOrder = orderRepository.save(new Order(request.getOrderTableId(), toOrderLineItems(request)));
        return new OrderResponse(savedOrder);
    }

    private void validateEmptyOrderTable(final Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> toOrderLineItems(final OrderSaveRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(this::toOrderLineItem)
                .collect(toList());
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemSaveRequest it) {
        Menu menu = getById(it.getMenuId());
        return new OrderLineItem(menu.getName(), menu.getPrice().getValue(), it.getQuantity());
    }

    private Menu getById(final Long id) {
        return menuRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeOrderStatusRequest request) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(request.getOrderStatus());
        return new OrderResponse(savedOrder);
    }
}
