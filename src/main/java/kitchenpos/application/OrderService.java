package kitchenpos.application;

import static java.util.stream.Collectors.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.OrderChangeOrderStatusRequest;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemCreateRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderVerifier;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;
    private final OrderVerifier orderVerifier;

    public OrderService(
        final OrderRepository orderRepository,
        final OrderLineItemRepository orderLineItemRepository,
        final OrderTableRepository orderTableRepository,
        final MenuRepository menuRepository,
        final OrderVerifier orderVerifier
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
        this.orderVerifier = orderVerifier;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        final OrderTable orderTable = orderTableRepository
            .findById(orderCreateRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        final List<OrderLineItem> orderLineItems = orderCreateRequest.getOrderLineItems()
            .stream()
            .map(OrderLineItemCreateRequest::toEntity)
            .collect(Collectors.toList());

        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문 항목 개수와 메뉴 개수는 같아야 합니다.");
        }

        orderVerifier.verify(orderTable, orderLineItems);

        final Order savedOrder = orderRepository.save(orderCreateRequest.toEntity());

        List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.saveAll(
            orderLineItems.stream()
                .peek(it -> it.changeOrderId(savedOrder.getId()))
                .collect(Collectors.toList())
        );

        return OrderResponse.of(savedOrder, savedOrderLineItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        Map<Long, Order> allOrders = orderRepository.findAll()
            .stream()
            .collect(Collectors.toMap(Order::getId, it -> it));
        Map<Order, List<OrderLineItem>> orderLineItemsGroup = orderLineItemRepository
            .findAllByOrderIdIn(allOrders.keySet()).stream()
            .collect(groupingBy(it -> allOrders.get(it.getOrderId())));

        return allOrders.values()
            .stream()
            .map(it -> OrderResponse.of(
                it,
                orderLineItemsGroup.getOrDefault(it, Collections.emptyList())
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(
        final Long orderId,
        final OrderChangeOrderStatusRequest orderChangeOrderStatusRequest
    ) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderChangeOrderStatusRequest.getOrderStatus());

        return OrderResponse.of(savedOrder, orderLineItemRepository.findAllByOrderId(savedOrder.getId()));
    }
}
