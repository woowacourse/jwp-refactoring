package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.Repository.MenuRepository;
import kitchenpos.Repository.OrderLineItemRepository;
import kitchenpos.Repository.OrderRepository;
import kitchenpos.Repository.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.request.OrderRequest;
import kitchenpos.ui.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository, final OrderLineItemRepository orderLineItemRepository,
        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems()
            .stream()
            .map(value -> new OrderLineItem(menuRepository.findById(value.getMenuId()).orElseThrow(IllegalArgumentException::new), value.getQuantity()))
            .collect(Collectors.toList());
        Order order = new Order(
            orderTableRepository.findById(request.getOrderTableId()).orElseThrow(IllegalArgumentException::new), orderLineItems);

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::menuId)
            .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        order.clearId();

        final OrderTable orderTable = orderTableRepository.findById(order.orderTableId())
            .orElseThrow(IllegalArgumentException::new);

        orderTable.validateEmpty();
        order.changeOrderStats(orderTable, OrderStatus.COOKING, LocalDateTime.now());

        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(savedOrder);
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        savedOrder.changeOrderLineItems(savedOrderLineItems);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.changeOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.validateStatus(OrderStatus.COMPLETION);

        final OrderStatus orderStatus = OrderStatus.valueOf(order.orderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        orderRepository.save(savedOrder);

        savedOrder.changeOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return OrderResponse.from(savedOrder);
    }
}
