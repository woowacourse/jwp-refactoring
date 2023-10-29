package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemQuantity;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.request.OrderChangeStatusRequest;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.response.OrderLineItemResponse;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemCreateRequest> orderLineItemRequest = request.getOrderLineItems();
        validateOrderLineItemSize(orderLineItemRequest);
        final List<Long> menuIds = getMenuIds(orderLineItemRequest);
        validateDuplicateMenu(orderLineItemRequest, menuIds);

        final OrderTable orderTable = findOrderTableById(request.getOrderTableId());
        orderTable.validateIsEmpty();

        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        final Order savedOrder = orderRepository.save(order);
        saveOrderLineItems(orderLineItemRequest, savedOrder);
        return convertToResponse(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        final Order order = findOrderById(orderId);
        order.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return convertToResponse(order);
    }

    private void validateDuplicateMenu(final List<OrderLineItemCreateRequest> orderLineItemRequest,
                                       final List<Long> menuIds) {
        if (orderLineItemRequest.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItemSize(final List<OrderLineItemCreateRequest> orderLineItemRequest) {
        if (CollectionUtils.isEmpty(orderLineItemRequest)) {
            throw new IllegalArgumentException();
        }
    }

    private void saveOrderLineItems(final List<OrderLineItemCreateRequest> orderLineItemRequest, final Order order) {
        for (final OrderLineItemCreateRequest orderLineItem : orderLineItemRequest) {
            final Menu menu = findMenuById(orderLineItem.getMenuId());
            orderLineItemRepository.save(new OrderLineItem(
                    order,
                    new MenuName(menu.getName()),
                    new MenuPrice(menu.getPrice()),
                    new OrderLineItemQuantity(orderLineItem.getQuantity())
            ));
        }
    }

    private List<Long> getMenuIds(final List<OrderLineItemCreateRequest> orderLineItemRequest) {
        return orderLineItemRequest.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(Objects.requireNonNull(orderTableId))
                .orElseThrow(IllegalArgumentException::new);
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(Objects.requireNonNull(orderId))
                .orElseThrow(IllegalArgumentException::new);
    }

    private Menu findMenuById(final Long menuId) {
        return menuRepository.findById(Objects.requireNonNull(menuId))
                .orElseThrow(IllegalArgumentException::new);
    }

    private OrderResponse convertToResponse(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTable().getId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                orderLineItemRepository.findAllByOrderId(order.getId()).stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList())
        );
    }

    private OrderLineItemResponse convertToResponse(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getMenuName().getName(),
                orderLineItem.getMenuPrice().getPrice(),
                orderLineItem.getQuantity()
        );
    }
}
