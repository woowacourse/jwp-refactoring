package kitchenpos.order.application;

import kitchenpos.order.application.dto.OrderLineItemDto;
import kitchenpos.order.application.dto.OrderStatusDto;
import kitchenpos.order.application.dto.request.OrderCreateRequest;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.application.mapper.OrderMapper;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.order.persistence.OrderLineItemRepository;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemDto> orderLineItems = request.getOrderLineItems();
        checkValidOrderLineItems(orderLineItems);

        final OrderTable orderTable = findOrderTableById(request.getOrderTableId());
        final Order savedOrder = orderRepository.save(new Order(orderTable));
        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(orderLineItems, savedOrder);

        return OrderMapper.mapToOrderResponseBy(savedOrder, savedOrderLineItems);
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

    private List<OrderLineItem> saveOrderLineItems(final List<OrderLineItemDto> orderLineItemDtos, final Order order) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            final Menu savedMenu = menuRepository.findById(orderLineItemDto.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            orderLineItems.add(new OrderLineItem(order, savedMenu.getId(), orderLineItemDto.getQuantity()));
        }
        return orderLineItemRepository.saveAll(orderLineItems);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> {
                    final List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
                    return OrderMapper.mapToOrderResponseBy(order, savedOrderLineItems);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusDto status) {
        final Order order = findOrderById(orderId);
        order.changeStatus(OrderStatus.valueOf(status.getOrderStatus()));
        final Order savedOrder = orderRepository.save(order);
        final List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.findAllByOrderId(savedOrder.getId());

        return OrderMapper.mapToOrderResponseBy(savedOrder, savedOrderLineItems);
    }

    private Order findOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
