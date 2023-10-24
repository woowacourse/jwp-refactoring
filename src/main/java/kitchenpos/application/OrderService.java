package kitchenpos.application;

import kitchenpos.application.dto.OrderLineItemDto;
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
    public Order create(final OrderCreateRequest request) {
        final List<OrderLineItemDto> orderLineItemDtos = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order order = new Order(orderTable, OrderStatus.COOKING);
        final Order savedOrder = orderRepository.save(order);

        for (final OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            final Menu savedMenu = menuRepository.findById(orderLineItemDto.getMenuId()).get();
            final OrderLineItem orderLineItem = new OrderLineItem(savedOrder, savedMenu, orderLineItemDto.getQuantity());
            orderLineItemRepository.save(orderLineItem);
        }

        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        //for (final Order order : orders) {
        //    order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        //}

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final Order updatedOrder = new Order(savedOrder.getId(), savedOrder.getOrderTable(), orderStatus, savedOrder.getOrderedTime());
        return orderRepository.save(updatedOrder);
    }
}
