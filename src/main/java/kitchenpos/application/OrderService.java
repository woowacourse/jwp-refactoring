package kitchenpos.application;

import kitchenpos.application.dto.OrderLineItemDto;
import kitchenpos.application.dto.OrderStatusDto;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.domain.*;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.OrderLineItemRepository;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
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
    public Order create(final OrderCreateRequest request) {
        final List<OrderLineItemDto> orderLineItems = request.getOrderLineItems();
        checkValidOrderLineItems(orderLineItems);

        final OrderTable orderTable = findOrderTableById(request.getOrderTableId());
        final Order savedOrder = orderRepository.save(new Order(orderTable));
        saveOrderLineItems(orderLineItems, savedOrder);
        return savedOrder;
    }

    private void checkValidOrderLineItems(final List<OrderLineItemDto> orderLineItems) {
        checkEmptyOrder(orderLineItems);
        checkNotExistOrderItems(orderLineItems);
    }

    private void checkEmptyOrder(final List<OrderLineItemDto> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNotExistOrderItems(final List<OrderLineItemDto> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void saveOrderLineItems(final List<OrderLineItemDto> orderLineItems, final Order order) {
        for (final OrderLineItemDto orderLineItem : orderLineItems) {
            final Menu savedMenu = menuRepository.findById(orderLineItem.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            orderLineItemRepository.save(new OrderLineItem(order, savedMenu, orderLineItem.getQuantity()));
        }
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        //for (final Order order : orders) {
        //    order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        //}

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusDto status) {
        final Order order = findOrderById(orderId);
        order.changeStatus(OrderStatus.valueOf(status.getOrderStatus()));
        return orderRepository.save(order);
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
