package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

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
    public Order create(final Order order) {
        final OrderLineItems orderLineItems = order.getOrderLineItems();

        validOrderLineItemsIsEmpty(orderLineItems);
        validOrderLineItemsHaveNotExistMenu(orderLineItems);

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        validOrderTableIsEmpty(orderTable);

        final Order savedOrder = orderRepository.save(
            new Order(
                null,
                orderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                order.getOrderLineItems()
            )
        );

        associateOrderLineItemsAndOrder(orderLineItems, savedOrder);

        return savedOrder;
    }

    private void validOrderLineItemsIsEmpty(OrderLineItems orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validOrderLineItemsHaveNotExistMenu(OrderLineItems orderLineItems) {
        final List<Long> menuIds = orderLineItems.extractMenuIds();

        if (orderLineItems.hasNotSize(menuRepository.countByIdIn(menuIds))) {
            throw new IllegalArgumentException();
        }
    }

    private void validOrderTableIsEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void associateOrderLineItemsAndOrder(OrderLineItems orderLineItems, Order savedOrder) {
        orderLineItems.associateOrder(savedOrder.getId());
        List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.saveAll(orderLineItems.getOrderLineItems());
        savedOrder.setOrderLineItems(new OrderLineItems(savedOrderLineItems));
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        // orderRepository.save(savedOrder);

        return savedOrder;
    }
}
