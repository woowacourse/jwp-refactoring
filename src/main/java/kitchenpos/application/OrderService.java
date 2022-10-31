package kitchenpos.application;

import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final OrderLineItems orderLineItems = new OrderLineItems(orderRequest.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(null, it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList()));

        final List<Long> menuIds = mapToMenuIds(orderRequest);
        if (!orderLineItems.isSameMenuSize(menuRepository.countByIdIn(menuIds))) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order order = orderRepository.save(new Order(orderTable, orderRequest.getOrderStatus(),
                orderRequest.getOrderedTime()));

        final List<OrderLineItem> savedOrderLineItems = orderLineItems.getOrderLineItems()
                .stream()
                .map(orderLineItem -> orderLineItemRepository.save(new OrderLineItem(order,
                        orderLineItem.getMenuId(), orderLineItem.getQuantity())))
                .collect(Collectors.toList());

        return new Order(order.getId(), order.getOrderTable(), order.getOrderStatus(),
                order.getOrderedTime(), savedOrderLineItems);
    }

    private List<Long> mapToMenuIds(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> new Order(order.getId(), order.getOrderTable(),
                        order.getOrderStatus(), order.getOrderedTime(), orderLineItemRepository.findAllByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());

        return orderRepository.save(new Order(savedOrder.getId(), savedOrder.getOrderTable(), orderStatus.name(),
                savedOrder.getOrderedTime(),
                orderLineItemRepository.findAllByOrderId(orderId)));
    }
}
