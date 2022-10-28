package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderStatusRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository) {

        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = toOrderLineItems(orderRequest.getOrderLineItems());
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        validateOrderLineItemIsNotEmpty(orderLineItems);
        validateOrderLineItemIsNotDuplicatedAndExist(orderRequest.getOrderLineItems(), orderLineItems);
        validateOrderTableIsNotEmpty(orderTable);

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        return orderRepository.save(order);
    }

    private void validateOrderTableIsNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItemIsNotEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItemIsNotDuplicatedAndExist(
            List<OrderLineItemRequest> orderLineItemRequests, List<OrderLineItem> orderLineItems) {

        if (orderLineItems.size() != orderLineItemRequests.size()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(this::toOrderLineItem)
                .distinct()
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                .orElseThrow(IllegalArgumentException::new);

        return new OrderLineItem(menu, orderLineItemRequest.getQuantity());
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrder.isCompletion()) {
            throw new IllegalArgumentException();
        }

        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderStatusRequest.getOrderStatus()));
        return savedOrder;
    }
}
