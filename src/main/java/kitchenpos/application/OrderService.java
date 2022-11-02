package kitchenpos.application;

import static kitchenpos.domain.order.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.order.OrderedMenu;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderLineItemResponse;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderStatusUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        validateOrderLineItemsExist(orderLineItemRequests);
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        final List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList());
        final Order order = Order.of(orderTable, COOKING, LocalDateTime.now(), orderLineItems);
        orderRepository.save(order);

        return generateOrderResponse(order);
    }

    private OrderLineItem createOrderLineItem(final OrderLineItemRequest orderLineItemRequest) {
        final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                .orElseThrow(IllegalArgumentException::new);
        final OrderedMenu orderedMenu = new OrderedMenu(menu.getName(), menu.getPrice());
        return new OrderLineItem(orderedMenu, orderLineItemRequest.getQuantity());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::generateOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.updateOrderStatus(orderStatus);

        return generateOrderResponse(savedOrder);
    }

    private void validateOrderLineItemsExist(final List<OrderLineItemRequest> orderLineItemRequests) {
        if (orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderResponse generateOrderResponse(final Order savedOrder) {
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderTable().getId(),
                savedOrder.getOrderStatus().name(),
                savedOrder.getOrderedTime(),
                savedOrder.getOrderLineItems()
                        .stream()
                        .map(it -> new OrderLineItemResponse(
                                it.getSeq(),
                                it.getOrderedMenu().getName(),
                                it.getOrderedMenu().getPrice().getAmount(),
                                it.getQuantity()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
