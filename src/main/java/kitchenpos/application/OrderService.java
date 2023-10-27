package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.OrderChangeRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemCreateRequest;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        List<OrderLineItem> items = new ArrayList<>();
        for (OrderLineItemCreateRequest orderLineItem : request.getOrderLineItems()) {
            validateMenuExistsAndAddItems(items, orderLineItem);
        }
        OrderLineItems orderLineItems = new OrderLineItems(items);
        validateSize(request, orderLineItems);
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        validateEmpty(orderTable);
        return OrderResponse.toResponse(orderRepository.save(new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems)));
    }

    private void validateMenuExistsAndAddItems(List<OrderLineItem> items, OrderLineItemCreateRequest orderLineItem) {
        validateMenuExists(orderLineItem);
        addItems(items, orderLineItem);
    }

    private void addItems(List<OrderLineItem> items, OrderLineItemCreateRequest orderLineItem) {
        Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(IllegalArgumentException::new);
        items.add(new OrderLineItem(menu.getId(), orderLineItem.getQuantity(), menu.getName(), menu.getPrice()));
    }

    private void validateMenuExists(OrderLineItemCreateRequest orderLineItem) {
        if (!menuRepository.existsById(orderLineItem.getMenuId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSize(OrderCreateRequest request, OrderLineItems orderLineItems) {
        final List<Long> menuIds = request.getOrderLineItems().stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
        if (!orderLineItems.hasSameSizeWith(menuRepository.countByIdIn(menuIds))) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);
        return OrderResponse.toResponse(savedOrder);
    }
}
