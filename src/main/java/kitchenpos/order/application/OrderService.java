package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.OrderedMenu;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
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
            final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        final Order order = Order.ofUnsaved(orderTable);

        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        final List<OrderLineItem> orderLineItems = getOrderLineItemsOf(order, orderLineItemRequests);
        order.changeOrderLineItems(orderLineItems);

        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = request.getOrderStatus();
        savedOrder.changeStatus(orderStatus);

        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> getOrderLineItemsOf(final Order order,
                                                    final List<OrderLineItemRequest> orderLineItemRequests) {
        checkItemsHasEachMenu(orderLineItemRequests);
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> getOrderLineItemOf(order, orderLineItemRequest))
                .collect(Collectors.toList());
    }

    private void checkItemsHasEachMenu(final List<OrderLineItemRequest> orderLineItemRequests) {
        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderLineItem getOrderLineItemOf(final Order order, final OrderLineItemRequest orderLineItemRequest) {
        final Menu menu = menuRepository.getOne(orderLineItemRequest.getMenuId());
        final OrderedMenu orderedMenu = new OrderedMenu(menu.getId(), menu.getName(), menu.getPrice());
        return OrderLineItem.ofUnsaved(order, orderedMenu, orderLineItemRequest.getQuantity());
    }
}
