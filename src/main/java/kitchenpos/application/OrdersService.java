package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.OrdersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class OrdersService {

    private final MenuRepository menuRepository;
    private final OrdersRepository ordersRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrdersService(
        final MenuRepository menuRepository,
        final OrdersRepository ordersRepository,
        final OrderLineItemRepository orderLineItemRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.ordersRepository = ordersRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Orders create(final Orders requestOrders) {
        final List<OrderLineItem> orderLineItems = requestOrders.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(requestOrders.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Orders savedOrders = ordersRepository.save(
            new Orders(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now())
        );

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        savedOrders.add(savedOrderLineItems);

        return savedOrders;
    }

    public List<Orders> list() {
        final List<Orders> orders = ordersRepository.findAll();
        for (final Orders order : orders) {
            order.add(orderLineItemRepository.findAllByOrdersId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Orders changeOrderStatus(final Long orderId, final Orders orders) {
        final Orders savedOrders = ordersRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrders.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(orders.getOrderStatus());
        savedOrders.changeStatus(orderStatus.name());

        Orders changedOrders = ordersRepository.save(savedOrders);

        changedOrders.add(orderLineItemRepository.findAllByOrdersId(orderId));

        return changedOrders;
    }
}
