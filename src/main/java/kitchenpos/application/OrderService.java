package kitchenpos.application;

import kitchenpos.domain.order.*;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItem> requestOrderLineItems = orderRequest.toOrderLineItems();
        final OrderLineItems orderLineItems = OrderLineItems.of(requestOrderLineItems);

        validateRequestOrderLineItem(orderLineItems);

        validateOrderWithEmptyTable(orderRequest);

        final Order order = orderRequest.toOrder();
        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItemResponse> orderLineItemResponses = saveOrderLineItem(requestOrderLineItems, savedOrder);

        return OrderResponse.of(savedOrder, orderLineItemResponses);
    }

    private void validateRequestOrderLineItem(final OrderLineItems orderLineItems) {
        final List<Long> menuIds = orderLineItems.getMenuIds();
        final int requestMenuCount = menuRepository.countByIdIn(menuIds);
        if (orderLineItems.isSameMenuCount(requestMenuCount)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderWithEmptyTable(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmptyTable()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItemResponse> saveOrderLineItem(final List<OrderLineItem> requestOrderLineItems,
                                                          final Order savedOrder) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>(requestOrderLineItems.size());
        for (final OrderLineItem orderLineItem : requestOrderLineItems) {
            orderLineItem.addOrder(savedOrder);
            orderLineItems.add(orderLineItem);
        }
        final List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.saveAll(orderLineItems);

        return savedOrderLineItems.stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return getOrderResponses(orders);
    }

    private List<OrderResponse> getOrderResponses(final List<Order> orders) {
        final List<OrderResponse> orderResponses = new ArrayList<>();
        for (final Order order : orders) {
            List<OrderLineItemResponse> orderLineItemResponses = orderLineItemToResponse(order);
            orderResponses.add(OrderResponse.of(order, orderLineItemResponses));
        }
        return orderResponses;
    }

    private List<OrderLineItemResponse> orderLineItemToResponse(Order order) {
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
        return orderLineItems.stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);
        final Order changeStatusOrder = orderRepository.save(savedOrder);
        final List<OrderLineItemResponse> orderLineItemResponses = orderLineItemToResponse(changeStatusOrder);

        return OrderResponse.of(changeStatusOrder, orderLineItemResponses);
    }
}
