package kitchenpos.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderEditRequest;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderResponses;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.Table;

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
    public Long create(final OrderCreateRequest request) {
        List<OrderLineItemDto> orderLineItemDtos = request.getOrderLineItemDtos();

        final List<Long> menuIds = orderLineItemDtos.stream()
            .map(OrderLineItemDto::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuRepository.countByIdIn(menuIds) || orderLineItemDtos.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Table table = orderTableRepository.findById(request.getTableId())
            .orElseThrow(IllegalArgumentException::new);

        Order order = new Order(table, OrderStatus.COOKING);

        final Order savedOrder = orderRepository.save(order);

        // todo 엔티티관련 옵션있었는데 찾아보기.
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItemDto orderLineItemDto : orderLineItemDtos) {
            OrderLineItem orderLineItem = new OrderLineItem(order, orderLineItemDto.getMenuId(),
                orderLineItemDto.getQuantity());
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        savedOrder.changeOrderLineItems(savedOrderLineItems);

        return savedOrder.getId();
    }

    public OrderResponses list() {
        List<Order> orders = orderRepository.findAllWithOrderLineItems();
        return OrderResponses.from(orders);
    }

    @Transactional
    public void changeOrderStatus(Long orderId, OrderEditRequest request) {
        final Order savedOrder = findOne(orderId);
        savedOrder.changeOrderStatus(request.getOrderStatus());
    }

    private Order findOne(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
