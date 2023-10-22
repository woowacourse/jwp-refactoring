package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 메뉴가 존재하지 않습니다.");
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("상품이 존재하지 않습니다.");
        }

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블의 상태가 비어있습니다.");
        }
        final Order savedOrder = orderRepository.save(new Order(orderTable.getId(), OrderStatus.COOKING.name()));

        List<OrderLineItem> updatedOrderLineItems = new ArrayList<>();

        for (final OrderLineItem orderLineItem : orderLineItems) {
            final OrderLineItem updatedOrderLineItem = new OrderLineItem(
                    savedOrder,
                    orderLineItem.getMenuId(),
                    orderLineItem.getQuantity()
            );
            updatedOrderLineItems.add(updatedOrderLineItem);
        }

        for (final OrderLineItem updatedOrderLineItem : updatedOrderLineItems) {
            savedOrder.addOrderLineItem(orderLineItemRepository.save(updatedOrderLineItem));
        }

        return savedOrder;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        final Order updatedOrder = new Order(savedOrder.getId(), savedOrder.getOrderTableId(), orderStatus.name());

        return orderRepository.save(updatedOrder);
    }
}
